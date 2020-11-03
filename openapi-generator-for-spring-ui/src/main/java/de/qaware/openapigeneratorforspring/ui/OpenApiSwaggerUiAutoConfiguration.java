package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiIndexHtmlWebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(OpenApiSwaggerUiConfigurationProperties.class)
public class OpenApiSwaggerUiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerUiIndexHtmlWebJarResourceTransformerFactory swaggerUiIndexHtmlWebJarResourceTransformerFactory(
            OpenApiConfigurationProperties openApiConfigurationProperties
    ) {
        return new SwaggerUiIndexHtmlWebJarResourceTransformerFactory(openApiConfigurationProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SwaggerUiSupport swaggerUiSupport(OpenApiSwaggerUiConfigurationProperties openApiSwaggerUiConfigurationProperties) {
        return new SwaggerUiSupport(openApiSwaggerUiConfigurationProperties);
    }

}
