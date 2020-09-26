package de.qaware.openapigeneratorforspring.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.AbstractOpenApiResource;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;

public class OpenApiResourceForWebFlux extends AbstractOpenApiResource {

    static final ThreadLocal<ServerHttpRequest> SERVER_HTTP_REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public OpenApiResourceForWebFlux(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier objectMapperSupplier) {
        super(openApiGenerator, objectMapperSupplier);
    }

    @GetMapping(value = API_DOCS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOpenApiAsJson(ServerHttpRequest serverHttpRequest) throws JsonProcessingException {
        SERVER_HTTP_REQUEST_THREAD_LOCAL.set(serverHttpRequest);
        return super.getOpenApiAsJson();
    }
}
