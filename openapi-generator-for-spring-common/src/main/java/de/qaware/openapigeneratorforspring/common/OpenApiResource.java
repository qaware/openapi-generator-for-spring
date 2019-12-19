package de.qaware.openapigeneratorforspring.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiResource {

    @GetMapping(value = "/v3/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOpenApiAsJson() throws JsonProcessingException {
        return Json.mapper().writeValueAsString(new OpenAPI());
    }

}
