package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.OpenApiResourceParameterProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

public class OpenApiGeneratorWebFluxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResource openApiResource(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier openApiObjectMapperSupplier) {
        return new OpenApiResource(openApiGenerator, openApiObjectMapperSupplier);
    }

    @Bean
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new HandlerMethodsProviderFromWebFlux(requestMappingHandlerMapping);
    }

    @Bean
    public OpenApiResourceParameterProvider openApiResourceParameterProviderForWebFlux() {
        return new OpenApiResourceParameterProviderForWebFlux();
    }
}
