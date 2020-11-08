package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.supplier.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorSupplierAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public OpenApiObjectMapperSupplier defaultOpenApiObjectMapperSupplier() {
        return new DefaultOpenApiObjectMapperSupplier();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiSpringBootApplicationClassSupplier defaultOpenApiSpringBootApplicationClassSupplier() {
        return new DefaultOpenApiSpringBootApplicationClassSupplier();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiSpringBootApplicationAnnotationsSupplier defaultOpenApiSpringBootApplicationAnnotationsSupplier(
            OpenApiSpringBootApplicationClassSupplier springBootApplicationClassSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOpenApiSpringBootApplicationAnnotationsSupplier(springBootApplicationClassSupplier, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenAPIDefinitionAnnotationSupplier defaultOpenAPIDefinitionAnnotationSupplier(
            OpenApiSpringBootApplicationAnnotationsSupplier openApiSpringBootApplicationAnnotationsSupplier
    ) {
        return new DefaultOpenAPIDefinitionAnnotationSupplier(openApiSpringBootApplicationAnnotationsSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiDefaultServersSupplier defaultOpenApiDefaultServerSupplier(
            OpenApiBaseUriSupplier openApiBaseUriSupplier // provided by WebMVC or WebFlux
    ) {
        return new DefaultOpenApiDefaultServerSupplier(openApiBaseUriSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiDefaultMediaTypeSupplier defaultOpenApiDefaultMediaTypeSupplier() {
        return new DefaultOpenApiDefaultMediaTypeSupplier();
    }
}
