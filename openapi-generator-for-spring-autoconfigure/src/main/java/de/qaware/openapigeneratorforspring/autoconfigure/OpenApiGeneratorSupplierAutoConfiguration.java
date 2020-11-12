package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiDefaultMediaTypeSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiDefaultServerSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiSpringBootApplicationClassSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiDefaultMediaTypeSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiDefaultServersSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationClassSupplier;
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
