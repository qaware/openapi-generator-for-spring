package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class GenericTypeResolverForSchemaAnnotation implements GenericTypeResolver {

    // make it higher than others to prefer annotation implementation type
    public static final int ORDER = DEFAULT_ORDER - 100;

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation = annotationsSupplier.findFirstAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        if (schemaAnnotation != null && !Void.class.equals(schemaAnnotation.implementation())) {
            JavaType javaTypeFromSchemaAnnotation = openApiObjectMapperSupplier.get().constructType(schemaAnnotation.implementation());
            // TODO think about correct precedence of annotations, maybe make it customizable?
            AnnotationsSupplier annotationsSupplierWithSchemaAnnotation = annotationsSupplier
                    // prevent infinite loop by detecting the same annotation again and again
                    .withExcludedBy(schemaAnnotation::equals)
                    .andThen(annotationsSupplierFactory.createFromAnnotatedElement(schemaAnnotation.implementation()));
            schemaBuilderFromType.buildSchemaFromType(
                    javaTypeFromSchemaAnnotation,
                    annotationsSupplierWithSchemaAnnotation,
                    schemaConsumer
            );
            return true;
        }

        return false;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
