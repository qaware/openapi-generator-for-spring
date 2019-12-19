package de.qaware.openapigeneratorforspring.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class OpenApiGeneratorForSpringAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResource openApiResource(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new OpenApiResource(requestMappingHandlerMapping);
    }

}
