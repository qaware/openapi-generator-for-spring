package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver.InitialSchema;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        AtomicReference<Schema> schemaHolder = new AtomicReference<>();
        context.buildSchemaFromType(javaType, annotationsSupplier, schemaHolder::set, 0);
        context.resolveReferencedSchemas();
        return schemaHolder.get();
    }

    @RequiredArgsConstructor
    private class Context {

        private final SchemaAnnotationMapper schemaAnnotationMapper;
        private final ReferencedSchemaConsumer referencedSchemaConsumer;
        private final ReferencedSchemas referencedSchemas = new ReferencedSchemas();

        void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer, int recursionDepth) {
            for (TypeResolver typeResolver : typeResolvers) {
                boolean resolved = typeResolver.resolveFromType(
                        javaType, annotationsSupplier,
                        // this allows the TypeResolver to control recursion
                        (type, supplier, consumer) -> buildSchemaFromType(type, supplier, consumer, recursionDepth + 1),
                        schemaConsumer
                );
                if (resolved) {
                    return;
                }
            }

            InitialSchema initialSchema = buildSchemaFromTypeWithoutProperties(javaType, annotationsSupplier);
            Schema schemaWithoutProperties = initialSchema.getSchema();
            Map<String, AnnotatedMember> properties = schemaPropertiesResolver.findProperties(javaType);
            List<Consumer<Schema>> schemaReferenceSetters = referencedSchemas.findSchemaReference(schemaWithoutProperties, properties.keySet());
            if (schemaReferenceSetters != null) {
                // we've seen this initialSchema before, then simply reference it lazily
                schemaReferenceSetters.add(schemaConsumer);
            } else {
                // important to add the schemaWithoutProperties first before traversing the nested properties (if any)
                // this prevents infinite loops when types refer to themselves
                ReferencedSchemas.PropertyConsumer propertyConsumer = referencedSchemas.addNewSchemaReference(
                        schemaWithoutProperties,
                        schemaConsumer,
                        recursionDepth == 0,
                        properties.keySet(),
                        ReferencedSchemas.CustomizerSupport.of(javaType, annotationsSupplier)
                );
                if (properties.isEmpty() || initialSchema.isIgnoreNestedProperties()) {
                    return;
                }
                properties.forEach((propertyName, member) -> {
                    AnnotationsSupplier propertyAnnotationsSupplier = annotationsSupplierFactory.createFromMember(member)
                            .andThen(annotationsSupplierFactory.createFromAnnotatedElement(member.getType().getRawClass()));
                    Consumer<Schema> propertySchemaCustomizer = propertyConsumer.consumeProperty(propertyName, member.getType(), propertyAnnotationsSupplier);
                    // here the recursion happens!
                    buildSchemaFromType(member.getType(), propertyAnnotationsSupplier,
                            propertySchemaCustomizer.andThen(propertySchema -> schemaWithoutProperties.addProperty(propertyName, propertySchema)),
                            recursionDepth + 1
                    );
                });
            }
        }

        private InitialSchema buildSchemaFromTypeWithoutProperties(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            InitialSchema initialSchema = getSchemaFromInitialTypeResolvers(javaType);

            schemaCustomizers.forEach(customizer -> customizer.customize(initialSchema.getSchema(), javaType, annotationsSupplier, null));

            // applying the schemaAnnotationMapper is treated specially here:
            // 1) It can only be built with an existing SchemaResolver (this class!)
            //    so that would end up in a circular loop if it wasn't resolved by using the schemaAnnotationMapperFactory
            // 2) Using it requires a referencedSchemaConsumer, something which is only present during building and not as a singleton
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

        void resolveReferencedSchemas() {
            referencedSchemas.forEach(referencedSchema -> {

//                schemaCustomizers.forEach(referencedSchema::callCustomizer);

                Schema schema = referencedSchema.getSchema();
                int schemaReferenceConsumersSize = referencedSchema.getReferenceConsumersSize();
                if (schemaReferenceConsumersSize > 1 && referencedSchema.hasProperties()) {
                    // already set (not globally unique) reference here to have a "comparable" schema after this resolution
                    // globally unique reference identifier will be set after all schemas are collected
                    referencedSchema.consumeSchema(schema);
                    referencedSchemaConsumer.alwaysAsReference(schema,
                            // globally unique reference identifier is known finally, then set it at all places
                            referencedSchema::consumeSchema
                    );
                    return;
                }

                if (referencedSchema.isTopLevel()) {
                    // just one reference and on top-level,
                    // so that will be given to referencedSchemaConsumer later
                    referencedSchema.consumeSchema(schema);
                } else {
                    // "maybeAsReference" sets the schema immediately by calling consumeSchema. This ensures that the returned schema
                    // is completely built before returning and allows proper "schema equality" comparisons later
                    // the consumeSchema might be called again if it was decided later that
                    // the schema is referenced instead of being inlined
                    referencedSchemaConsumer.maybeAsReference(schema, referencedSchema::consumeSchema);
                }

            });
        }
    }

    private static class ReferencedSchemas {

        @RequiredArgsConstructor
        private static class ReferencedSchema {
            @Getter
            private final Schema schema;
            @Getter
            private final boolean topLevel;
            private final Set<String> propertyNames;
            private final LinkedList<Consumer<Schema>> referenceConsumers;
            private final CustomizerSupport customizerSupport;
            private final Map<String, CustomizerSupport> properties = new LinkedHashMap<>();

            void addProperty(String propertyName, CustomizerSupport propertyCustomizerSupport) {
                properties.put(propertyName, propertyCustomizerSupport);
            }

            boolean hasProperties() {
                return !properties.isEmpty();
            }

            int getReferenceConsumersSize() {
                return referenceConsumers.size();
            }

            void callCustomizer(SchemaCustomizer customizer) {
                customizer.customize(schema, customizerSupport.type, customizerSupport.annotationsSupplier, properties);
            }

            void consumeSchema(Schema newSchema) {
                // as schemas are referenced and the storage compares schema including properties,
                // it's important to run the consumers such that the last encountered consumer is accepted first
                referenceConsumers.descendingIterator().forEachRemaining(schemaConsumer -> schemaConsumer.accept(newSchema));
            }
        }

        @RequiredArgsConstructor(staticName = "of")
        private static class CustomizerSupport implements SchemaCustomizer.SchemaProperty {
            @Getter
            private final JavaType type;
            @Getter
            private final AnnotationsSupplier annotationsSupplier;
            private final List<Consumer<Schema>> propertyCustomizers = new ArrayList<>();

            void customizePropertySchema(Schema propertySchema) {
                propertyCustomizers.forEach(customizer -> customizer.accept(propertySchema));
            }

            @Override
            public void customize(Consumer<Schema> propertySchemaCustomizer) {
                propertyCustomizers.add(propertySchemaCustomizer);
            }
        }

        private final LinkedList<ReferencedSchema> items = new LinkedList<>();

        void forEach(Consumer<ReferencedSchema> action) {
            items.descendingIterator().forEachRemaining(referencedSchema -> {
                if (referencedSchema.schema.isEmpty()) {
                    throw new IllegalStateException("Encountered completely empty schema");
                }
                if (referencedSchema.referenceConsumers.isEmpty()) {
                    throw new IllegalStateException("Encountered schema without any schema consumers, that's strange");
                }
                action.accept(referencedSchema);
            });
        }

        @Nullable
        List<Consumer<Schema>> findSchemaReference(Schema schema, Set<String> propertyNames) {
            ensurePropertiesAreNowhereSet(schema);

            return items.stream()
                    .filter(referencedSchema -> referencedSchema.schema.equals(schema)
                            && referencedSchema.propertyNames.equals(propertyNames)) // schema name is not part of "equals"!
                    .reduce((a, b) -> {
                        throw new IllegalStateException("Found two entries for " + schema + ": " + a + " vs. " + b);
                    })
                    .map(referencedSchema -> referencedSchema.referenceConsumers)
                    .orElse(null);
        }

        void ensurePropertiesAreNowhereSet(Schema schema) {
            // safe-guard against wrong implementation of GenericTypeResolvers
            // they must defer setting properties until resolveReferencedSchemas is called
            if (schema.getProperties() != null) {
                throw new IllegalStateException("To be added schema has non-null properties");
            }
            items.stream().map(ReferencedSchema::getSchema).forEach(existingSchema -> {
                if (existingSchema.getProperties() != null) {
                    throw new IllegalStateException("Existing referenced schema has non-null properties");
                }
            });
        }

        PropertyConsumer addNewSchemaReference(Schema schema, Consumer<Schema> firstSchemaConsumer, boolean isTopLevel, Set<String> propertyNames, CustomizerSupport customizerSupport) {
            ReferencedSchema referencedSchema = new ReferencedSchema(
                    schema,
                    isTopLevel,
                    propertyNames,
                    new LinkedList<>(Collections.singleton(firstSchemaConsumer)),
                    customizerSupport
            );
            items.add(referencedSchema);
            return (propertyName, propertyType, propertyAnnotationsSupplier) -> {
                CustomizerSupport propertyCustomizerSupport = CustomizerSupport.of(propertyType, propertyAnnotationsSupplier);
                referencedSchema.addProperty(propertyName, propertyCustomizerSupport);
                // return customizer for this property
                // the list of customizers is filled when the schema is finally built
                return propertyCustomizerSupport::customizePropertySchema;
            };
        }

        @FunctionalInterface
        interface PropertyConsumer {
            Consumer<Schema> consumeProperty(String propertyName, JavaType propertyType, AnnotationsSupplier propertyAnnotationsSupplier);
        }
    }

}
