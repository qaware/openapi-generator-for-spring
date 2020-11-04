package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
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
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder
    ) {
        return new HandlerMethodsProviderForWebMvc(requestMappingHandlerMapping, springWebHandlerMethodBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiRequestAwareSupplierForWebMvc openApiRequestAwareProviderForWebMvc(
            WebRequest webRequest,
            HttpServletRequest httpServletRequest
    ) {
        return new OpenApiRequestAwareSupplierForWebMvc(webRequest, httpServletRequest);
    }
}
