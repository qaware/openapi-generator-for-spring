package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenApiGeneratorWebMvcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Conditional(OpenApiConfigurationProperties.EnabledCondition.class)
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
    public OpenApiRequestAwareProviderForWebMvc openApiRequestAwareProviderForWebMvc(
            WebRequest webRequest,
            HttpServletRequest httpServletRequest
    ) {
        return new OpenApiRequestAwareProviderForWebMvc(webRequest, httpServletRequest);
    }
}
