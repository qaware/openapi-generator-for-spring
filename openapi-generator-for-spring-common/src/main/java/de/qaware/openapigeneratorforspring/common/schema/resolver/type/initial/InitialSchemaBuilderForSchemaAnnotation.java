package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier.Purpose.SCHEMA_BUILDING;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.earlierThan;

@RequiredArgsConstructor
public class InitialSchemaBuilderForSchemaAnnotation implements InitialSchemaBuilder {

    // make this higher precedence as the implementation from the @Schema annotation should override anything else
    public static final int ORDER = earlierThan(DEFAULT_ORDER);

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver resolver) {
        return annotationsSupplier.findAnnotations(Schema.class)
                .findFirst()
                .map(Schema::implementation)
                .filter(clazz -> !Void.class.equals(clazz))
                .map(clazz -> resolver.resolveFromType(
                        openApiObjectMapperSupplier.get(SCHEMA_BUILDING).constructType(clazz),
                        annotationsSupplierFactory.createFromAnnotatedElement(clazz)
                ))
                .orElse(null);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
