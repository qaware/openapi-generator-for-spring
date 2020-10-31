package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.OpenApiResourceParameterProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenApiGeneratorWebMvcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResourceForWebMvc openApiResource(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier openApiObjectMapperSupplier) {
        return new OpenApiResourceForWebMvc(openApiGenerator, openApiObjectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new HandlerMethodsProviderForWebMvc(requestMappingHandlerMapping);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResourceParameterProvider openApiResourceParameterProviderForWebMvc(WebRequest webRequest) {
        return new OpenApiResourceParameterProviderForWebMvc(webRequest);
    }
}
