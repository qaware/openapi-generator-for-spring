package de.qaware.openapigeneratorforspring.common.schema;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import io.swagger.v3.oas.models.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultSchemaResolver implements SchemaResolver {

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final SchemaAnnotationMapperFactory schemaAnnotationMapperFactory;

    @Override
    public Schema<Object> resolveFromClass(Class<?> clazz, NestedSchemaConsumer nestedSchemaConsumer) {
        ObjectMapper mapper = openApiObjectMapperSupplier.get();
        ReferencedSchemas referencedSchemas = new ReferencedSchemas();

        JavaType javaType = mapper.constructType(clazz);

        AnnotationsSupplierForClass annotationsSupplier = new AnnotationsSupplierForClass(clazz);

        Schema<Object> schema = buildSchemaFromTypeWithoutProperties(javaType, annotationsSupplier, nestedSchemaConsumer);
        addPropertiesToSchema(javaType, schema, referencedSchemas, nestedSchemaConsumer);

        referencedSchemas.referencedSchemas.forEach(referencedSchema -> nestedSchemaConsumer.consume(
                referencedSchema.schema,
                referenceName -> referencedSchema.reference.set$ref(referenceName.asUniqueString())
        ));

        return schema;
    }

    private Schema<Object> buildSchemaFromTypeWithoutProperties(JavaType javaType, AnnotationsSupplier annotationsSupplier, NestedSchemaConsumer nestedSchemaConsumer) {
        // TODO do some primitive type handling here
        Schema<Object> schema = new Schema<>();

        schema.setName(javaType.getRawClass().getSimpleName());

        // TODO support other nullable annotations?
        if (annotationsSupplier.findAnnotations(Nullable.class).findFirst().isPresent()) {
            schema.setNullable(true);
        }

        // TODO create this once
        SchemaAnnotationMapper schemaAnnotationMapper = schemaAnnotationMapperFactory.create(this);

        annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                .collect(Collectors.toCollection(LinkedList::new))
                .descendingIterator()
                .forEachRemaining(schemaAnnotation -> schemaAnnotationMapper.applyFromAnnotation(schema, schemaAnnotation, nestedSchemaConsumer));

        return schema;
    }

    private void addPropertiesToSchema(JavaType javaType, Schema<Object> schema, ReferencedSchemas referencedSchemas, NestedSchemaConsumer nestedSchemaConsumer) {

        BeanDescription beanDesc = getBeanDescription(javaType);
        Set<String> ignoredPropertyNames = beanDesc.getIgnoredPropertyNames();

        for (BeanPropertyDefinition propDef : beanDesc.findProperties()) {
            if (ignoredPropertyNames.contains(propDef.getName())) {
                continue;
            }


            AnnotatedMember member = propDef.getAccessor();

            Schema<Object> propertySchema = buildSchemaFromTypeWithoutProperties(member.getType(), new AnnotationSupplierForMember(member), nestedSchemaConsumer);

            Schema<?> schemaReference = referencedSchemas.findSchemaReferenceIgnoringProperties(propertySchema);

            if (schemaReference != null) {
                schema.addProperties(propDef.getName(), schemaReference);
            } else {
                Schema<?> newSchemaReference = referencedSchemas.addNewSchemaAndCreateReference(propertySchema);
                addPropertiesToSchema(member.getType(), propertySchema, referencedSchemas, nestedSchemaConsumer);
                schema.addProperties(propDef.getName(), newSchemaReference);
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
            private final Schema<Object> schema;
            private final Schema<?> reference = new Schema<>();
        }

        private final List<ReferencedSchema> referencedSchemas = new ArrayList<>();

        @Nullable
        public Schema<?> findSchemaReferenceIgnoringProperties(Schema<Object> schema) {
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
                    .map(referencedSchema -> referencedSchema.reference)
                    .orElse(null);
        }

        public Schema<?> addNewSchemaAndCreateReference(Schema<Object> schema) {
            ReferencedSchema referencedSchema = new ReferencedSchema(schema);
            referencedSchemas.add(referencedSchema);
            return referencedSchema.reference;
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
