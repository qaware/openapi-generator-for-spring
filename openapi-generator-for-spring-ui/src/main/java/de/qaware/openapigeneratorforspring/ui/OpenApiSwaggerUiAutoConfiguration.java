package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiIndexHtmlWebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport;
import de.qaware.openapigeneratorforspring.ui.webjar.DefaultWebJarTransformedResourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Instant;

@EnableConfigurationProperties(OpenApiSwaggerUiConfigurationProperties.class)
public class OpenApiSwaggerUiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerUiIndexHtmlWebJarResourceTransformerFactory swaggerUiIndexHtmlWebJarResourceTransformerFactory(
            OpenApiConfigurationProperties openApiConfigurationProperties,
            OpenApiSwaggerUiApiDocsUrisSupplier swaggerUiApiDocsUrisSupplier
    ) {
        return new SwaggerUiIndexHtmlWebJarResourceTransformerFactory(openApiConfigurationProperties, swaggerUiApiDocsUrisSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public SwaggerUiSupport swaggerUiSupport(OpenApiSwaggerUiConfigurationProperties openApiSwaggerUiConfigurationProperties) {
        return new SwaggerUiSupport(openApiSwaggerUiConfigurationProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiSwaggerUiApiDocsUrisSupplier openApiSwaggerUiApiDocsUrisSupplier() {
        return new DefaultOpenApiSwaggerUiApiDocsUrisSupplier();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultWebJarTransformedResourceBuilder defaultWebJarTransformedResourceBuilder(OpenApiSwaggerUiConfigurationProperties openApiSwaggerUiConfigurationProperties) {
        return new DefaultWebJarTransformedResourceBuilder(openApiSwaggerUiConfigurationProperties, Instant::now);
    }
}
