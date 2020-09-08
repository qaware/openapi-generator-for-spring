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
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.SimpleTypeResolver;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;

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
    private final List<GenericTypeResolver> genericTypeResolvers;
    private final List<SimpleTypeResolver> simpleTypeResolvers;
    private final List<SchemaCustomizer> schemaCustomizers;

    @Override
    public Schema resolveFromClass(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer) {
        return resolveFromType(clazz, annotationsSupplierFactory.createFromAnnotatedElement(clazz), referencedSchemaConsumer);
    }

    @Override
    public Schema resolveFromType(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer) {
        ObjectMapper mapper = openApiObjectMapperSupplier.get();
        Context context = new Context(mapper, schemaAnnotationMapperFactory.create(this), referencedSchemaConsumer);
        JavaType javaType = mapper.constructType(type);
        AtomicReference<Schema> schema = new AtomicReference<>();
        context.buildSchemaFromType(javaType, annotationsSupplier, schema::set);
        context.resolveReferencedSchemas();
        return schema.get();
    }

    @RequiredArgsConstructor
    private class Context implements SchemaBuilderFromType {

        private final ObjectMapper mapper;
        private final SchemaAnnotationMapper schemaAnnotationMapper;
        private final ReferencedSchemaConsumer referencedSchemaConsumer;
        private final ReferencedSchemas referencedSchemas = new ReferencedSchemas();

        @Override
        public void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer) {
            for (GenericTypeResolver genericTypeResolver : genericTypeResolvers) {
                if (genericTypeResolver.resolveFromType(javaType, annotationsSupplier, this, schemaConsumer)) {
                    return;
                }
            }

            Schema newSchema = buildSchemaFromTypeWithoutProperties(javaType, annotationsSupplier);
            List<Consumer<Schema>> schemaReferenceSetters = referencedSchemas.findSchemaReferenceIgnoringProperties(newSchema);
            if (schemaReferenceSetters != null) {
                // we've seen this newSchema before, then simply reference it lazily
                schemaReferenceSetters.add(schemaConsumer);
            } else {
                // important to add the newSchema first before traversing the nested properties
                // this prevents infinite loops when types refer to themselves
                referencedSchemas.addNewSchemaReference(newSchema, schemaConsumer);
                addPropertiesToSchema(javaType, newSchema);
            }
        }

        public Schema buildSchemaFromTypeWithoutProperties(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            Schema schema = getSchemaFromSimpleTypeResolvers(javaType);

            for (SchemaCustomizer schemaCustomizer : schemaCustomizers) {
                schemaCustomizer.customize(schema, javaType, annotationsSupplier);
            }

            // applying the schemaAnnotationMapper is treated specially here:
            // 1) It can only be built with an existing SchemaResolver (this class!)
            //    so that would end up in a circular loop if it wasn't resolved by using the SchemaResolverFactory
            // 2) Using it requires a referencedSchemaConsumer, something which is only present on invocation
            annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(schemaAnnotation -> schemaAnnotationMapper.applyFromAnnotation(schema, schemaAnnotation, referencedSchemaConsumer));

            return schema;
        }

        private Schema getSchemaFromSimpleTypeResolvers(JavaType javaType) {
            for (SimpleTypeResolver simpleTypeResolver : simpleTypeResolvers) {
                Schema resolvedSchema = simpleTypeResolver.resolveFromType(javaType);
                if (resolvedSchema != null) {
                    return resolvedSchema;
                }
            }
            throw new IllegalStateException("No simple type resolver found for " + javaType);
        }

        public void addPropertiesToSchema(JavaType javaType, Schema schema) {
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
                                propertySchema -> schema.addProperties(property.getName(), propertySchema));
                    });
        }

        public void resolveReferencedSchemas() {
            referencedSchemas.items.forEach(
                    referencedSchema -> {
                        if (referencedSchema.referenceConsumers.size() == 1 || referencedSchema.schema.getName() == null) {
                            // TODO make handling of "name == null" more flexible?
                            referencedSchema.referenceConsumers.forEach(schemaConsumer -> schemaConsumer.accept(referencedSchema.schema));
                        } else {
                            Schema schemaForReferenceName = new Schema();
                            referencedSchema.referenceConsumers.forEach(schemaConsumer -> schemaConsumer.accept(schemaForReferenceName));
                            referencedSchemaConsumer.consume(
                                    referencedSchema.schema,
                                    referenceName -> schemaForReferenceName.set$ref(referenceName.asUniqueString())
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

        public void addNewSchemaReference(Schema schema, Consumer<Schema> firstSchemaConsumer) {
            ReferencedSchema referencedSchema = new ReferencedSchema(
                    schema,
                    new ArrayList<>(Collections.singleton(firstSchemaConsumer))
            );
            items.add(referencedSchema);
        }
    }

}