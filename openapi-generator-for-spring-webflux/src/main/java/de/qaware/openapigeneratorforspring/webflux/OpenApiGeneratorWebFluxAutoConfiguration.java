package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiResourceParameterProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

public class OpenApiGeneratorWebFluxAutoConfiguration {
    @Bean
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new HandlerMethodsProviderFromWebFlux(requestMappingHandlerMapping);
    }

    @Bean
    public OpenApiResourceParameterProvider openApiResourceParameterProviderForWebFlux(ServerHttpRequest serverHttpRequest) {
        return new OpenApiResourceParameterProviderForWebFlux(serverHttpRequest);
    }
}
