package de.qaware.openapigeneratorforspring.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.OPEN_API_DOCS_DEFAULT_PATH;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RestController
@Hidden
public abstract class AbstractOpenApiResource {

    protected static final String API_DOCS_PATH_SPEL = "${" +
            // "api-docs-path" should match OpenApiConfigurationProperties setting
            CONFIG_PROPERTIES_PREFIX + ".api-docs-path:" +
            OPEN_API_DOCS_DEFAULT_PATH + "}";

    private final OpenApiGenerator openApiGenerator;
    private final OpenApiObjectMapperSupplier objectMapperSupplier;

    protected String getOpenApiAsJson() throws JsonProcessingException {
        OpenApi openApi = openApiGenerator.generateOpenApi();
        return objectMapperSupplier.get().writeValueAsString(openApi);
    }
}
