package de.qaware.openapigeneratorforspring.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Hidden
public class OpenApiResource {


    private final OpenApiGenerator openApiGenerator;
    private final OpenApiObjectMapperSupplier objectMapperSupplier;

    @GetMapping(value = "/v3/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOpenApiAsJson() throws JsonProcessingException {
        OpenAPI openApi = openApiGenerator.generateOpenApi();
        return objectMapperSupplier.get().writeValueAsString(openApi);
    }
}
