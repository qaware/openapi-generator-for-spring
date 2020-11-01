package de.qaware.openapigeneratorforspring.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.AbstractOpenApiResource;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;

public class OpenApiResourceForWebFlux extends AbstractOpenApiResource {

    /**
     * Using this thread local only works if the OpenApi model is built within
     * one thread (or the thread local state must be copied into possible
     * child threads).
     *
     * <p> This should be considered a workaround as WebFlux doesn't
     * offer good alternatives to {@code @RequestScope} beans (yet?).
     */
    static final ThreadLocal<ServerHttpRequest> SERVER_HTTP_REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public OpenApiResourceForWebFlux(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier objectMapperSupplier) {
        super(openApiGenerator, objectMapperSupplier);
    }

    @GetMapping(value = API_DOCS_PATH_SPEL, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOpenApiAsJson(ServerHttpRequest serverHttpRequest) throws JsonProcessingException {
        SERVER_HTTP_REQUEST_THREAD_LOCAL.set(serverHttpRequest);
        return super.getOpenApiAsJson();
    }
}
