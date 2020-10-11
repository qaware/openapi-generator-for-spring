package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.model.OpenApi;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface OpenApiCustomizer extends Ordered {
    int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    void customize(OpenApi openApi, @Nullable OpenAPIDefinition openAPIDefinitionAnnotation);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
