package de.qaware.openapigeneratorforspring.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiGeneratorForSpringAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResource openApiResource() {
        return new OpenApiResource();
    }

}
