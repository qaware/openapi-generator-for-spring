package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.BeanDescription;
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
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver.InitialSchema;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultSchemaResolver implements SchemaResolver {

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
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
            List<Consumer<Schema>> schemaReferenceSetters = referencedSchemas.findSchemaReferenceIgnoringProperties(schemaWithoutProperties);
            if (schemaReferenceSetters != null) {
                // we've seen this initialSchema before, then simply reference it lazily
                schemaReferenceSetters.add(schemaConsumer);
            } else {
                // important to add the schemaWithoutProperties first before traversing the nested properties (if any)
                // this prevents infinite loops when types refer to themselves
                referencedSchemas.addNewSchemaReference(schemaWithoutProperties, schemaConsumer, recursionDepth == 0);
                if (initialSchema.isHasNestedProperties()) {
                    addPropertiesToSchema(javaType, schemaWithoutProperties, recursionDepth);
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
            LinkedList<io.swagger.v3.oas.annotations.media.Schema> schemaAnnotations = annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                    .collect(Collectors.toCollection(LinkedList::new));
            schemaAnnotations
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

        private void addPropertiesToSchema(JavaType javaType, Schema schema, int recursionDepth) {
            BeanDescription beanDescriptionForType = mapper.getSerializationConfig().introspect(javaType);
            Set<String> ignoredPropertyNames = beanDescriptionForType.getIgnoredPropertyNames();
            beanDescriptionForType.findProperties().stream()
                    .filter(property -> !ignoredPropertyNames.contains(property.getName()))
                    .filter(property -> property.getAccessor() != null) // safe-guard weird types
                    .forEachOrdered(property -> {
                        AnnotatedMember member = property.getAccessor();
                        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromMember(member)
                                .andThen(annotationsSupplierFactory.createFromAnnotatedElement(member.getType().getRawClass()));
                        buildSchemaFromType(member.getType(), annotationsSupplier,
                                propertySchema -> schema.addProperties(property.getName(), propertySchema),
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
                            throw new IllegalStateException("Encountered schema without any schema consumers");
                        } else if (schemaReferenceConsumers.size() == 1 || StringUtils.isBlank(schema.getName())) {
                            // "maybeAsReference" already sets the schema immediately. This ensures that the returned schema
                            // is completely built before returning and allows "schema equality" comparisons later
                            // we still allow the referenceSchemaConsumer to turn that schema into a reference later on maybe
                            if (referencedSchema.isTopLevel) {
                                schemaReferenceConsumers.forEach(schemaConsumer -> schemaConsumer.accept(schema));
                            } else {
                                referencedSchemaConsumer.maybeAsReference(schema, maybeReferenceSchema ->
                                        // this lambda can be called again if it was decided later that
                                        // the schema is referenced instead of being inlined
                                        schemaReferenceConsumers.forEach(schemaConsumer -> schemaConsumer.accept(maybeReferenceSchema))
                                );
                            }
                        } else {
                            // already set (not globally unique) reference here to have a "comparable" schema after this resolution
                            // globally unique reference name will be set after all schemas are collected
                            schemaReferenceConsumers.forEach(schemaConsumer -> schemaConsumer.accept(schema));
                            referencedSchemaConsumer.alwaysAsReference(schema, referenceName ->
                                    // globally unique reference name is known finally, then set it finally
                                    schemaReferenceConsumers.forEach(schemaConsumer ->
                                            schemaConsumer.accept(new Schema().$ref(referenceName.asReferenceString())))
                            );
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
            private final List<Consumer<Schema>> referenceConsumers;
        }

        private final List<ReferencedSchema> items = new ArrayList<>();

        @Nullable
        public List<Consumer<Schema>> findSchemaReferenceIgnoringProperties(Schema schema) {
            return items.stream()
                    .filter(referencedSchema -> {
                        // Schema.equals ignores the name, that's why we check it here manually
                        if (!Objects.equals(referencedSchema.schema.getName(), schema.getName())) {
                            return false;
                        }
                        // safe-guard against wrong implementation of GenericTypeResolvers
                        // they must defer setting properties until resolveReferencedSchemas is called
                        if (schema.getProperties() != null) {
                            throw new IllegalStateException("To be added schema has non-null properties");
                        }
                        if (referencedSchema.schema.getProperties() != null) {
                            throw new IllegalStateException("Existing referenced schema has non-null properties");
                        }
                        return referencedSchema.schema.equals(schema);
                    })
                    .findFirst()
                    .map(referencedSchema -> referencedSchema.referenceConsumers)
                    .orElse(null);
        }

        public void addNewSchemaReference(Schema schema, Consumer<Schema> firstSchemaConsumer, boolean isTopLevel) {
            ReferencedSchema referencedSchema = new ReferencedSchema(
                    schema,
                    isTopLevel,
                    new ArrayList<>(Collections.singleton(firstSchemaConsumer))
            );
            items.add(referencedSchema);
        }
    }

}
