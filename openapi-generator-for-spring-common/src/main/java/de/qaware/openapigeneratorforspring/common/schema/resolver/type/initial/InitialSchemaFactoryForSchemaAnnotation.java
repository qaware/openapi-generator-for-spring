package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Optional;

@RequiredArgsConstructor
public class InitialSchemaFactoryForSchemaAnnotation implements InitialSchemaFactory {

    public static final int ORDER = DEFAULT_ORDER - 100; // make this higher precedence as the annotation should override everything

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialTypeResolver fallbackResolver) {
        return Optional.ofNullable(annotationsSupplier.findFirstAnnotation(Schema.class))
                .map(Schema::implementation)
                .filter(clazz -> !Void.class.equals(clazz))
                .map(clazz -> fallbackResolver.resolveFromType(
                        openApiObjectMapperSupplier.get().constructType(clazz),
                        annotationsSupplierFactory.createFromAnnotatedElement(clazz)
                ))
                .orElse(null);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
