package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver.InitialSchema;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultSchemaResolver implements SchemaResolver {

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final SchemaPropertiesResolver schemaPropertiesResolver;
    private final SchemaAnnotationMapperFactory schemaAnnotationMapperFactory;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final List<TypeResolver> typeResolvers;
    private final List<InitialTypeResolver> initialTypeResolvers;
    private final List<SchemaCustomizer> schemaCustomizers;

    @Override
    public void resolveFromClass(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer, Consumer<Schema> schemaSetter) {
        referencedSchemaConsumer.maybeAsReference(resolveFromClassWithoutReference(clazz, referencedSchemaConsumer), schemaSetter);
    }

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
        Context context = new Context(mapper, schemaAnnotationMapperFactory.create(this), referencedSchemaConsumer);
        JavaType javaType = mapper.constructType(type);
        AtomicReference<Schema> schemaHolder = new AtomicReference<>();
        context.buildSchemaFromType(javaType, annotationsSupplier, schemaHolder::set, 0);
        context.resolveReferencedSchemas();
        return schemaHolder.get();
    }

    @RequiredArgsConstructor
    private class Context {

        private final ObjectMapper mapper;
        private final SchemaAnnotationMapper schemaAnnotationMapper;
        private final ReferencedSchemaConsumer referencedSchemaConsumer;
        private final ReferencedSchemas referencedSchemas = new ReferencedSchemas();

        void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer, int recursionDepth) {
            for (TypeResolver typeResolver : typeResolvers) {
                boolean resolved = typeResolver.resolveFromType(
                        javaType, annotationsSupplier,
                        (type, supplier, consumer) -> buildSchemaFromType(type, supplier, consumer, recursionDepth + 1),
                        schemaConsumer
                );
                if (resolved) {
                    return;
                }
            }

            InitialSchema initialSchema = buildSchemaFromTypeWithoutProperties(javaType, annotationsSupplier);
            Schema schemaWithoutProperties = initialSchema.getSchema();
            List<Consumer<Schema>> schemaReferenceSetters = referencedSchemas.findSchemaReference(schemaWithoutProperties);
            if (schemaReferenceSetters != null) {
                // we've seen this initialSchema before, then simply reference it lazily
                schemaReferenceSetters.add(schemaConsumer);
            } else {
                Map<String, AnnotatedMember> properties = schemaPropertiesResolver.findProperties(javaType);
                boolean hasProperties = !properties.isEmpty() && !initialSchema.isIgnoreNestedProperties();
                // important to add the schemaWithoutProperties first before traversing the nested properties (if any)
                // this prevents infinite loops when types refer to themselves
                referencedSchemas.addNewSchemaReference(schemaWithoutProperties, schemaConsumer, recursionDepth == 0, hasProperties);
                if (hasProperties) {
                    addPropertiesToSchema(properties, schemaWithoutProperties, recursionDepth);
                }
            }
        }

        private InitialSchema buildSchemaFromTypeWithoutProperties(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            InitialSchema initialSchema = getSchemaFromInitialTypeResolvers(javaType);

            for (SchemaCustomizer schemaCustomizer : schemaCustomizers) {
                schemaCustomizer.customize(initialSchema.getSchema(), javaType, annotationsSupplier);
            }

            // applying the schemaAnnotationMapper is treated specially here:
            // 1) It can only be built with an existing SchemaResolver (this class!)
            //    so that would end up in a circular loop if it wasn't resolved by using the schemaAnnotationMapperFactory
            // 2) Using it requires a referencedSchemaConsumer, something which is only present on invocation
            annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                    // apply in reverse order
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(schemaAnnotation -> schemaAnnotationMapper.applyFromAnnotation(initialSchema.getSchema(), schemaAnnotation, referencedSchemaConsumer));

            return initialSchema;
        }

        private InitialSchema getSchemaFromInitialTypeResolvers(JavaType javaType) {
            for (InitialTypeResolver initialTypeResolver : initialTypeResolvers) {
                InitialSchema initialSchema = initialTypeResolver.resolveFromType(javaType);
                if (initialSchema != null) {
                    return initialSchema;
                }
            }
            throw new IllegalStateException("No initial type resolver found for " + javaType);
        }

        private void addPropertiesToSchema(Map<String, AnnotatedMember> properties, Schema schema, int recursionDepth) {
            properties.forEach((propertyName, member) -> {
                AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromMember(member)
                        .andThen(annotationsSupplierFactory.createFromAnnotatedElement(member.getType().getRawClass()));
                buildSchemaFromType(member.getType(), annotationsSupplier,
                        propertySchema -> schema.addProperties(propertyName, propertySchema),
                        recursionDepth + 1
                );
            });
        }

        void resolveReferencedSchemas() {
            referencedSchemas.items.forEach(
                    referencedSchema -> {
                        Schema schema = referencedSchema.schema;
                        List<Consumer<Schema>> schemaReferenceConsumers = referencedSchema.referenceConsumers;
                        if (schema.isEmpty()) {
                            throw new IllegalStateException("Encountered completely empty schema");
                        } else if (schemaReferenceConsumers.isEmpty()) {
                            throw new IllegalStateException("Encountered schema without any schema consumers, that's strange");
                        } else if (schemaReferenceConsumers.size() > 1 && referencedSchema.hasProperties) {
                            // already set (not globally unique) reference here to have a "comparable" schema after this resolution
                            // globally unique reference name will be set after all schemas are collected
                            schemaReferenceConsumers.forEach(schemaConsumer -> schemaConsumer.accept(schema));
                            referencedSchemaConsumer.alwaysAsReference(schema, referenceName ->
                                    // globally unique reference name is known finally, then set it at all places
                                    schemaReferenceConsumers.forEach(schemaConsumer ->
                                            schemaConsumer.accept(new Schema().$ref(referenceName.asReferenceString()))
                                    )
                            );
                        } else {
                            if (referencedSchema.isTopLevel) {
                                // just one reference and on top-level,
                                // so that will be given to referencedSchemaConsumer later
                                schemaReferenceConsumers.forEach(schemaConsumer -> schemaConsumer.accept(schema));
                            } else {
                                // "maybeAsReference" sets the schema immediately by calling the schemaSetter. This ensures that the returned schema
                                // is completely built before returning and allows proper "schema equality" comparisons later
                                referencedSchemaConsumer.maybeAsReference(schema, maybeReferencedSchema ->
                                        // the consumer might be called again if it was decided later that
                                        // the schema is referenced instead of being inlined
                                        schemaReferenceConsumers.forEach(schemaConsumer -> schemaConsumer.accept(maybeReferencedSchema)
                                        ));
                            }
                        }
                    }
            );
        }
    }

    private static class ReferencedSchemas {

        @RequiredArgsConstructor
        public static class ReferencedSchema {
            private final Schema schema;
            private final boolean isTopLevel;
            private final boolean hasProperties;
            private final List<Consumer<Schema>> referenceConsumers;
        }

        private final List<ReferencedSchema> items = new ArrayList<>();

        @Nullable
        public List<Consumer<Schema>> findSchemaReference(Schema schema) {
            // safe-guard against wrong implementation of GenericTypeResolvers
            // they must defer setting properties until resolveReferencedSchemas is called
            if (schema.getProperties() != null) {
                throw new IllegalStateException("To be added schema has non-null properties");
            }
            items.stream().map(referencedSchema -> referencedSchema.schema).forEach(existingSchema -> {
                if (existingSchema.getProperties() != null) {
                    throw new IllegalStateException("Existing referenced schema has non-null properties");
                }
            });

            return items.stream()
                    .filter(referencedSchema -> referencedSchema.schema.equals(schema)) // schema name is not part of "equals"!
                    .reduce((a, b) -> {
                        throw new IllegalStateException("Found two entries for " + schema + ": " + a + " vs. " + b);
                    })
                    .map(referencedSchema -> referencedSchema.referenceConsumers)
                    .orElse(null);
        }

        public void addNewSchemaReference(Schema schema, Consumer<Schema> firstSchemaConsumer, boolean isTopLevel, boolean hasProperties) {
            ReferencedSchema referencedSchema = new ReferencedSchema(
                    schema,
                    isTopLevel,
                    hasProperties,
                    new ArrayList<>(Collections.singleton(firstSchemaConsumer))
            );
            items.add(referencedSchema);
        }
    }

}
