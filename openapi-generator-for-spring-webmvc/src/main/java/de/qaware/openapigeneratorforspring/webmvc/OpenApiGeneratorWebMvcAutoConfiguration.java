package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiResourceParameterProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class OpenApiGeneratorWebMvcAutoConfiguration {
    @Bean
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new HandlerMethodsProviderFromWebMvc(requestMappingHandlerMapping);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResourceParameterProvider openApiResourceParameterProviderForWebMvc(WebRequest webRequest) {
        return new OpenApiResourceParameterProviderForWebMvc(webRequest);
    }
}
