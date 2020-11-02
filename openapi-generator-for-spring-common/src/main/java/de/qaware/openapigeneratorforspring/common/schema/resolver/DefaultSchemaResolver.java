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
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiLoggingUtils;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
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

import static de.qaware.openapigeneratorforspring.common.util.OpenApiLoggingUtils.logPretty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiLoggingUtils.prettyPrintSchema;

@RequiredArgsConstructor
@Slf4j
public class DefaultSchemaResolver implements SchemaResolver {

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final SchemaAnnotationMapperFactory schemaAnnotationMapperFactory;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final List<TypeResolver> typeResolvers;
    private final List<InitialSchemaBuilder> initialSchemaBuilders;
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
        return RecursiveSchema.unwrap(schema);
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
                if (recursionKey != null) {
                    return new RecursiveSchema(schema, recursionKey);
                }
                break;
            }
            return schema;
        }

        public void resolveReferencedSchemas() {
            knownSchemas.forEach((recursionKey, knownSchema) -> LOGGER.debug("Known Schema: {} -> {}", recursionKey.hashCode(), logPretty(knownSchema)));
            referencableSchemas.forEach(referencableSchema -> {
                LOGGER.debug("Resolving: {}", logPretty(referencableSchema.getSchema()));
                if (referencableSchema.getSchema() instanceof RecursiveSchema) {
                    RecursiveSchema recursiveSchema = (RecursiveSchema) referencableSchema.getSchema();
                    referencedSchemaConsumer.alwaysAsReference(recursiveSchema.getSchema(), referencableSchema.getSchemaConsumer());
                } else {
                    referencedSchemaConsumer.maybeAsReference(referencableSchema.getSchema(), referencableSchema.getSchemaConsumer());
                }
            });
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
            for (InitialSchemaBuilder initialSchemaBuilder : initialSchemaBuilders) {
                InitialSchema initialSchema = initialSchemaBuilder.buildFromType(javaType, annotationsSupplier, this::getSchemaFromInitialTypeResolvers);
                if (initialSchema != null) {
                    return initialSchema;
                }
            }
            throw new IllegalStateException("No initial type resolver found for " + javaType);
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
            LOGGER.debug("Setting {}", logPretty(schema));
            schemaConsumer.accept(schema);
            return ReferencableSchema.of(schema, schemaConsumer);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
    private static class RecursiveSchema extends Schema implements OpenApiLoggingUtils.HasToPrettyString {
        @Getter
        private final Schema schema;
        @EqualsAndHashCode.Include
        private final TypeResolver.RecursionKey recursionKey;

        public static Schema unwrap(Schema schema) {
            if (schema instanceof RecursiveSchema) {
                return ((RecursiveSchema) schema).getSchema();
            }
            return schema;
        }

        @Override
        public String getName() {
            return schema.getName();
        }

        @Override
        public String toPrettyString() {
            return "->" + prettyPrintSchema(schema) + "$" + recursionKey.hashCode();
        }
    }
}
