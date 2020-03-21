package de.qaware.openapigeneratorforspring.common.schema;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultSchemaResolver implements SchemaResolver {

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final SchemaAnnotationMapperFactory schemaAnnotationMapperFactory;

    @Override
    public Schema resolveFromClass(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer) {
        ObjectMapper mapper = openApiObjectMapperSupplier.get();
        ReferencedSchemas referencedSchemas = new ReferencedSchemas();

        JavaType javaType = mapper.constructType(clazz);

        AnnotationsSupplierForClass annotationsSupplier = new AnnotationsSupplierForClass(clazz);

        Schema schema = buildSchemaFromTypeWithoutProperties(javaType, annotationsSupplier, referencedSchemas, referencedSchemaConsumer);
        addPropertiesToSchema(javaType, schema, referencedSchemas, referencedSchemaConsumer);

        referencedSchemas.referencedSchemas.forEach(
                referencedSchema -> {
                    if (referencedSchema.referenceConsumers.size() == 1 || referencedSchema.schema.getName() == null) {
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

        return schema;
    }


    private Schema buildSchemaFromTypeWithoutProperties(JavaType javaType, AnnotationsSupplier annotationsSupplier, ReferencedSchemas referencedSchemas, ReferencedSchemaConsumer referencedSchemaConsumer) {

        if (javaType.isCollectionLikeType()) {
            Schema containerSchema = new Schema();
            containerSchema.setType("array");
            JavaType contentType = javaType.getContentType();
            // TODO adapt annotations supplier, consider @ArraySchema
            Schema schemaItems = buildSchemaFromTypeWithoutProperties(contentType, annotationsSupplier, referencedSchemas, referencedSchemaConsumer);

            addPropertiesToSchema(contentType, schemaItems, referencedSchemas, referencedSchemaConsumer);
            containerSchema.setItems(schemaItems);
            return containerSchema;
        }

        // TODO do some more primitive type handling here
        if (javaType.getRawClass().equals(String.class)) {
            // TODO properly handle "referenced" and "inline" schemas
            Schema schema = new Schema();
            schema.setType("string");
            return schema;
        }


        Schema schema = new Schema();
        schema.setType("object");
        schema.setName(javaType.getRawClass().getSimpleName());

        // TODO support other nullable annotations?
        if (annotationsSupplier.findAnnotations(Nullable.class).findFirst().isPresent()) {
            schema.setNullable(true);
        }

        // TODO create this once
        SchemaAnnotationMapper schemaAnnotationMapper = schemaAnnotationMapperFactory.create(this);

        // TODO respect schema implementation type if any is given
        annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                .collect(Collectors.toCollection(LinkedList::new))
                .descendingIterator()
                .forEachRemaining(schemaAnnotation -> schemaAnnotationMapper.applyFromAnnotation(schema, schemaAnnotation, referencedSchemaConsumer));

        return schema;
    }

    private void addPropertiesToSchema(JavaType javaType, Schema schema, ReferencedSchemas referencedSchemas, ReferencedSchemaConsumer referencedSchemaConsumer) {

        BeanDescription beanDesc = getBeanDescription(javaType);
        Set<String> ignoredPropertyNames = beanDesc.getIgnoredPropertyNames();

        for (BeanPropertyDefinition propDef : beanDesc.findProperties()) {
            if (ignoredPropertyNames.contains(propDef.getName())) {
                continue;
            }

            AnnotatedMember member = propDef.getAccessor();

            Schema propertySchema = buildSchemaFromTypeWithoutProperties(member.getType(), new AnnotationSupplierForMember(member), referencedSchemas, referencedSchemaConsumer);

            List<Consumer<Schema>> schemaReferenceSetters = referencedSchemas.findSchemaReferenceIgnoringProperties(propertySchema);

            if (schemaReferenceSetters != null) {
                // we've seen this propertySchema before, then simply reference it
                schemaReferenceSetters.add(schemaReference -> schema.addProperties(propDef.getName(), schemaReference));
            } else {
                // important to add the propertySchema first before traversing the nested properties
                referencedSchemas.addNewSchemaReference(
                        propertySchema,
                        schemaReference -> schema.addProperties(propDef.getName(), schemaReference)
                );
                addPropertiesToSchema(member.getType(), propertySchema, referencedSchemas, referencedSchemaConsumer);
            }
        }
    }

    private BeanDescription getBeanDescription(JavaType type) {
        // TODO reduce calls to supplier by passing mapper around
        ObjectMapper mapper = openApiObjectMapperSupplier.get();

        BeanDescription recurBeanDesc = mapper.getSerializationConfig().introspect(type);
        HashSet<String> visited = new HashSet<>();
        JsonSerialize jsonSerialize = recurBeanDesc.getClassAnnotations().get(JsonSerialize.class);

        while (jsonSerialize != null && !Void.class.equals(jsonSerialize.as())) {
            String asName = jsonSerialize.as().getName();
            if (visited.contains(asName)) {
                break;
            }
            visited.add(asName);

            recurBeanDesc = mapper.getSerializationConfig().introspect(
                    mapper.constructType(jsonSerialize.as())
            );
            jsonSerialize = recurBeanDesc.getClassAnnotations().get(JsonSerialize.class);
        }
        return recurBeanDesc;
    }


    private static class ReferencedSchemas {

        @RequiredArgsConstructor
        public static class ReferencedSchema {
            private final Schema schema;
            private final List<Consumer<Schema>> referenceConsumers;
        }

        private final List<ReferencedSchema> referencedSchemas = new ArrayList<>();

        @Nullable
        public List<Consumer<Schema>> findSchemaReferenceIgnoringProperties(Schema schema) {
            return referencedSchemas.stream()
                    .filter(referencedSchema -> {
                        // Schema.equals ignores the name, that's why we check it here manually
                        if (!Objects.equals(referencedSchema.schema.getName(), schema.getName())) {
                            return false;
                        }
                        Map<String, Schema> originalProperties = schema.getProperties();
                        schema.setProperties(referencedSchema.schema.getProperties());
                        boolean equalsIgnoringProperties = referencedSchema.schema.equals(schema);
                        schema.setProperties(originalProperties);
                        return equalsIgnoringProperties;
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
            referencedSchemas.add(referencedSchema);
        }
    }


    @FunctionalInterface
    private interface AnnotationsSupplier {
        <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType);
    }

    private static class AnnotationSupplierForMember implements AnnotationsSupplier {
        private final AnnotatedMember annotatedMember;
        private final AnnotationsSupplierForClass supplierForType;

        public AnnotationSupplierForMember(AnnotatedMember annotatedMember) {
            this.annotatedMember = annotatedMember;
            this.supplierForType = new AnnotationsSupplierForClass(annotatedMember.getType().getRawClass());
        }

        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            Stream<A> annotationFromMember = Optional.ofNullable(annotatedMember.getAnnotation(annotationType))
                    .map(Stream::of)
                    .orElse(Stream.empty());
            return Stream.concat(annotationFromMember, supplierForType.findAnnotations(annotationType));
        }
    }

    private static class AnnotationsSupplierForClass implements AnnotationsSupplier {
        private final MergedAnnotations mergedAnnotations;

        public AnnotationsSupplierForClass(Class<?> clazz) {
            this.mergedAnnotations = MergedAnnotations.from(clazz, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none());
        }

        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            return mergedAnnotations.stream(annotationType)
                    .map(MergedAnnotation::synthesize);
        }
    }

}
