package de.qaware.openapigeneratorforspring.common.util;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DefaultOpenAPIDefinitionAnnotationSupplier implements OpenAPIDefinitionAnnotationSupplier {

    private final OpenApiSpringBootApplicationAnnotationsSupplier openApiSpringBootApplicationAnnotationsSupplier;

    @Override
    public Optional<OpenAPIDefinition> get() {
        return openApiSpringBootApplicationAnnotationsSupplier.findFirstAnnotation(OpenAPIDefinition.class);
    }
}
