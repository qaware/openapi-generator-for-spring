package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class DefaultSchemaResolver implements SchemaResolver {

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final SchemaAnnotationMapperFactory schemaAnnotationMapperFactory;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final List<TypeResolver> typeResolvers;
    private final List<InitialSchemaFactory> initialSchemaFactories;
    private final List<SchemaCustomizer> schemaCustomizers;

    @Override
    public Schema resolveFromClassWithoutReference(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer) {
        return resolveFromTypeWithoutReference(clazz, annotationsSupplierFactory.createFromAnnotatedElement(clazz), referencedSchemaConsumer);
    }

    @Override
    public void resolveFromType(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer, Consumer<Schema> schemaSetter) {
        referencedSchemaConsumer.maybeAsReference(resolveFromTypeWithoutReference(type, annotationsSupplier, referencedSchemaConsumer), schemaSetter);
    }

    private Schema resolveFromTypeWithoutReference(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer) {
        ObjectMapper mapper = openApiObjectMapperSupplier.get();
        Context context = new Context(schemaAnnotationMapperFactory.create(this), referencedSchemaConsumer);
        JavaType javaType = mapper.constructType(type);
        Schema schema = context.buildSchemaFromTypeRecursively(javaType, annotationsSupplier);
        context.resolveReferencedSchemas();
        return schema;
    }

    @RequiredArgsConstructor
    private class Context implements RecursiveSchemaBuilder {

        private final SchemaAnnotationMapper schemaAnnotationMapper;
        private final ReferencedSchemaConsumer referencedSchemaConsumer;
        private final Map<TypeResolver.RecursionKey, Schema> knownSchemas = new LinkedHashMap<>();
        private final LinkedList<ReferencableSchema> referencableSchemas = new LinkedList<>();

        @Override
        public Schema buildSchemaFromTypeRecursively(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            InitialSchema initialSchema = buildInitialSchema(javaType, annotationsSupplier);
            Schema schema = initialSchema.getSchema();
            customizeSchema(schema, javaType, annotationsSupplier);

            TypeResolverActions actions = new TypeResolverActions();
            for (TypeResolver typeResolver : typeResolvers) {
                TypeResolver.RecursionKey recursionKey = typeResolver.resolve(initialSchema, javaType, annotationsSupplier, actions::add);
                if (actions.isEmpty()) {
                    continue;
                }
                // prevent depth-first recursion if we already know this schema
                Schema knownSchema = checkKnownSchema(schema, recursionKey);
                if (knownSchema != null) {
                    return knownSchema;
                }
                actions.runRecursively();
                break;
            }
            return schema;
        }

        @Nullable
        private Schema checkKnownSchema(Schema schema, @Nullable TypeResolver.RecursionKey recursionKey) {
            if (recursionKey == null) {
                return null;
            }
            Schema existingSchema = knownSchemas.get(recursionKey);
            if (existingSchema != null) {
                return new RecursiveSchema(existingSchema, recursionKey);
            }
            knownSchemas.put(recursionKey, schema);
            return null;
        }

        private void customizeSchema(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            schemaCustomizers.forEach(customizer -> customizer.customize(schema, javaType, annotationsSupplier));
        }

        private InitialSchema buildInitialSchema(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            InitialSchema initialSchema = getSchemaFromInitialTypeResolvers(javaType, annotationsSupplier);

            // applying the schemaAnnotationMapper is treated specially here:
            // 1) It can only be built with an existing SchemaResolver (this class!) due to discriminator mappings
            //    so that would end up in a circular loop if it wasn't resolved by using the schemaAnnotationMapperFactory
            // 2) Using it requires a referencedSchemaConsumer, something which is only present during building and not as a singleton bean
            annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                    // apply in reverse order
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(schemaAnnotation -> schemaAnnotationMapper.applyFromAnnotation(initialSchema.getSchema(), schemaAnnotation, referencedSchemaConsumer));

            return initialSchema;
        }

        private InitialSchema getSchemaFromInitialTypeResolvers(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            for (InitialSchemaFactory initialSchemaFactory : initialSchemaFactories) {
                InitialSchema initialSchema = initialSchemaFactory.resolveFromType(javaType, annotationsSupplier, this::getSchemaFromInitialTypeResolvers);
                if (initialSchema != null) {
                    return initialSchema;
                }
            }
            throw new IllegalStateException("No initial type resolver found for " + javaType);
        }

        void resolveReferencedSchemas() {
            knownSchemas.forEach((key, schema) -> {
                LOGGER.info("Known: {} -> {}", key.hashCode(), schema.toPrettyString());
            });
            LOGGER.info("Resolving:\n{}\n", referencableSchemas.stream().map(s -> s.getSchema().toPrettyString()).collect(Collectors.joining("\n")));
            referencableSchemas.forEach(referencableSchema -> {
                if (referencableSchema.getSchema() instanceof RecursiveSchema) {
                    RecursiveSchema recursiveSchema = (RecursiveSchema) referencableSchema.getSchema();
                    referencedSchemaConsumer.alwaysAsReference(recursiveSchema.getSchema(), referencableSchema.getSchemaConsumer());
                } else {
                    referencedSchemaConsumer.maybeAsReference(referencableSchema.getSchema(), referencableSchema.getSchemaConsumer());
                }
            });
            LOGGER.info("Resolved:\n{}\n", referencableSchemas.stream().map(s -> s.getSchema().toPrettyString()).collect(Collectors.joining("\n")));
        }

        private class TypeResolverActions extends LinkedList<TypeResolverAction> {

            void add(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer) {
                super.add(TypeResolverAction.of(javaType, annotationsSupplier, schemaConsumer));
            }

            void runRecursively() {
                forEach(action -> {
                    ReferencableSchema referencableSchema = action.run(Context.this);
                    referencableSchemas.add(referencableSchema);
                });
            }
        }
    }

    @FunctionalInterface
    private interface RecursiveSchemaBuilder {
        Schema buildSchemaFromTypeRecursively(JavaType javaType, AnnotationsSupplier annotationsSupplier);
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class ReferencableSchema {
        private final Schema schema;
        private final Consumer<Schema> schemaConsumer;
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class TypeResolverAction {
        private final JavaType javaType;
        private final AnnotationsSupplier annotationsSupplier;
        private final Consumer<Schema> schemaConsumer;

        ReferencableSchema run(RecursiveSchemaBuilder recursiveSchemaBuilder) {
            Schema schema = recursiveSchemaBuilder.buildSchemaFromTypeRecursively(javaType, annotationsSupplier);
            schemaConsumer.accept(schema);
            return ReferencableSchema.of(schema, schemaConsumer);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class RecursiveSchema extends Schema {
        @Getter
        private final Schema schema;
        private final TypeResolver.RecursionKey recursionKey;


        @Override
        public void setItems(Schema items) {
            schema.setItems(items);
        }

        @Override
        public String getName() {
            return schema.getName();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof RecursiveSchema) {
                LOGGER.info("Matching {} against {}", toPrettyString(), ((RecursiveSchema) o).toPrettyString());
                if (recursionKey.equals(((RecursiveSchema) o).recursionKey)) {
                    LOGGER.info("Matched against {}", ((RecursiveSchema) o).toPrettyString());
                    return true;
                }
                return false;
            } else {
                if (o instanceof Schema) {
                    LOGGER.info("Matching {} against {}", toPrettyString(), ((Schema) o).toPrettyString());
                    boolean equals = schema.equals(o);
                    if (equals) {
                        LOGGER.info("Matched against {}", ((Schema) o).toPrettyString());
                    }
                    return equals;
                }
                return false;
            }
        }

        @Override
        public String toPrettyString() {
            return "->" + schema.toPrettyString(false) + "$" + hashCode();
        }

        @Override
        public int hashCode() {
            return recursionKey.hashCode();
        }

        @Override
        public String toString() {
            return "RecursiveSchema(" + recursionKey.hashCode() + ")";
        }
    }
}
