package de.qaware.openapigeneratorforspring.common.info;


import de.qaware.openapigeneratorforspring.model.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import javax.annotation.Nullable;

public interface OpenApiInfoSupplier {
    Info get(@Nullable OpenAPIDefinition openAPIDefinitionAnnotation);
}
