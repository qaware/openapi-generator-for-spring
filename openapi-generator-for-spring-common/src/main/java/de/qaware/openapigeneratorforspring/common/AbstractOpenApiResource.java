package de.qaware.openapigeneratorforspring.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RestController
@Hidden
public abstract class AbstractOpenApiResource {

    protected static final String API_DOCS_PATH = "/v3/api-docs";

    private final OpenApiGenerator openApiGenerator;
    private final OpenApiObjectMapperSupplier objectMapperSupplier;

    protected String getOpenApiAsJson() throws JsonProcessingException {
        OpenAPI openApi = openApiGenerator.generateOpenApi();
        return objectMapperSupplier.get().writeValueAsString(openApi);
    }
}
