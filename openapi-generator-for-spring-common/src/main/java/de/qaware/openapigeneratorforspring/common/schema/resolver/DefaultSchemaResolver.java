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
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
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
        Schema schema = context.buildSchemaFromTypeRecursively(javaType, annotationsSupplier).getRight();
        context.resolveReferencedSchemas();
        return schema;
    }


    private static class SchemaBuildActions extends ArrayList<SchemaBuildActions.SchemaBuildAction> {

        public void add(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer) {
            super.add(SchemaBuildAction.of(javaType, annotationsSupplier, schemaConsumer));
        }

        @RequiredArgsConstructor(staticName = "of")
        private static class SchemaBuildAction {
            private final JavaType javaType;
            private final AnnotationsSupplier annotationsSupplier;
            private final Consumer<Schema> schemaConsumer;
        }
    }



    @RequiredArgsConstructor
    private class Context {

        private final SchemaAnnotationMapper schemaAnnotationMapper;
        private final ReferencedSchemaConsumer referencedSchemaConsumer;
        private final DefaultSchemaResolverSupport defaultSchemaResolverSupport = new DefaultSchemaResolverSupport();
        private final Map<Object, Schema> knownSchemas = new HashMap<>();
        private final LinkedList<Triple<Boolean, Schema, Consumer<Schema>>> referencableSchemas = new LinkedList<>();

        Pair<Object, Schema> buildSchemaFromTypeRecursively(JavaType javaType, AnnotationsSupplier annotationsSupplier) {

            InitialSchema initialSchema = buildSchemaFromTypeWithoutProperties(javaType, annotationsSupplier);
            Schema schemaWithoutProperties = initialSchema.getSchema();

            customizeSchema(schemaWithoutProperties, javaType, annotationsSupplier);

            SchemaBuildActions actions = new SchemaBuildActions();
            Object key = null;
            for (TypeResolver typeResolver : typeResolvers) {
                key = typeResolver.resolve(initialSchema, javaType, annotationsSupplier, actions::add);
                if (!actions.isEmpty()) {
                    break;
                }
            }

//            UniqueSchemaKey uniqueSchemaKey = new UniqueSchemaKey(
//                    schemaWithoutProperties.hashCode(),
//                    properties.keySet(),
//                    Stream.concat(Stream.of(javaType), actions.stream().map(a -> a.javaType)).collect(Collectors.toList())
//            );
            if (key != null) {
                Schema existingSchema = knownSchemas.get(key);
                if (existingSchema != null) {
                    return Pair.of(key, existingSchema);
                }
                knownSchemas.put(key, schemaWithoutProperties);
            }

            for (SchemaBuildActions.SchemaBuildAction action : actions) {
                Pair<Object, Schema> schemaPair = buildSchemaFromTypeRecursively(action.javaType, action.annotationsSupplier);
                if (schemaPair.getLeft() != null) {
                    action.schemaConsumer.accept(Schema.builder().name("MustReference_" + schemaPair.getLeft().hashCode()).build());
                } else {
                    action.schemaConsumer.accept(schemaPair.getRight());
                }
                referencableSchemas.add(Triple.of(schemaPair.getLeft() != null, schemaPair.getRight(), action.schemaConsumer));
            }

            return Pair.of(null, schemaWithoutProperties);

//            for (TypeResolver typeResolver : typeResolvers) {
//                boolean resolved = typeResolver.resolveFromType(
//                        javaType, annotationsSupplier, schemaConsumer,
//                        // this allows the TypeResolver to control recursion
//                        (type, supplier, consumer) -> buildSchemaFromType(type, supplier, consumer, recursionDepth),
//                        (type, supplier, consumer) -> buildSchemaFromTypeRecursively(type, supplier, consumer, recursionDepth + 1)
//                );
//                if (resolved) {
//                    return;
//                }
//            }
//            buildSchemaFromType(javaType, annotationsSupplier, schemaConsumer, recursionDepth);

//            return null;
        }

        private void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer, int recursionDepth) {

//            InitialSchema initialSchema = buildSchemaFromTypeWithoutProperties(javaType, annotationsSupplier);
//            Schema schemaWithoutProperties = initialSchema.getSchema();
//            Map<String, AnnotatedMember> properties = initialSchema.getProperties();
//
//            Map<String, PropertyCustomizer> propertyCustomizers = customizeSchema(schemaWithoutProperties, javaType, annotationsSupplier, properties);

//            defaultSchemaResolverSupport.consumeSchemaWithProperties(schemaWithoutProperties, properties.keySet(), recursionDepth == 0, schemaConsumer,
//                    () -> new LinkedList<>(properties.keySet()).descendingIterator().forEachRemaining(propertyName -> {
//                        AnnotatedMember member = properties.get(propertyName);
//                        AnnotationsSupplier propertyAnnotationsSupplier = annotationsSupplierFactory.createFromMember(member)
//                                .andThen(annotationsSupplierFactory.createFromAnnotatedElement(member.getType().getRawClass()));
//                        PropertyCustomizer propertyCustomizer = propertyCustomizers.get(propertyName);
//                        // here the recursion happens!
//                        buildSchemaFromTypeRecursively(member.getType(), propertyAnnotationsSupplier,
//                                propertySchema -> schemaWithoutProperties.setProperty(propertyName,
//                                        propertyCustomizer.customize(propertySchema, member.getType(), propertyAnnotationsSupplier)),
//                                recursionDepth + 1
//                        );
//                    })
//            );
        }

        private void customizeSchema(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            schemaCustomizers.forEach(customizer -> customizer.customize(schema, javaType, annotationsSupplier));
        }


        private InitialSchema buildSchemaFromTypeWithoutProperties(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
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
            referencableSchemas.descendingIterator().forEachRemaining(referencableSchema -> {
                if (referencableSchema.getLeft()) {
                    referencedSchemaConsumer.alwaysAsReference(referencableSchema.getMiddle(), referencableSchema.getRight());
                } else {
                    referencedSchemaConsumer.maybeAsReference(referencableSchema.getMiddle(), referencableSchema.getRight());
                }
            });

//            defaultSchemaResolverSupport.forEach(referencedSchema -> {
//                Schema schema = referencedSchema.getSchema();
//                if (referencedSchema.mustBeReferenced()) {
//                    // already set (not globally unique) reference here to have a "comparable" schema after this resolution
//                    // globally unique reference identifier will be set after all schemas are collected
//                    referencedSchema.consumeSchema(schema);
//                    referencedSchemaConsumer.alwaysAsReference(schema,
//                            // globally unique reference identifier is known finally, then set it at all places
//                            referencedSchema::consumeSchema
//                    );
//                } else if (referencedSchema.isTopLevel()) {
//                    // just one reference and on top-level,
//                    // so let the caller decide if the final schema should or must be referenced
//                    referencedSchema.consumeSchema(schema);
//                } else {
//                    // "maybeAsReference" sets the schema immediately by calling consumeSchema. This ensures that the returned schema
//                    // is completely built before returning and allows proper "schema equality" comparisons later
//                    // the consumeSchema might be called again if it was decided later that
//                    // the schema is referenced instead of being inlined
//                    referencedSchemaConsumer.maybeAsReference(schema, referencedSchema::consumeSchema);
//                }
//            });
        }
    }
}
