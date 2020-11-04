package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.InitialSchemaBuilderForFlux;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.InitialSchemaBuilderForMono;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForFlux;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class OpenApiGeneratorWebFluxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Conditional(OpenApiConfigurationProperties.EnabledCondition.class)
    public OpenApiResourceForWebFlux openApiResource(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier openApiObjectMapperSupplier) {
        return new OpenApiResourceForWebFlux(openApiGenerator, openApiObjectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder
    ) {
        return new HandlerMethodsProviderForWebFlux(requestMappingHandlerMapping, springWebHandlerMethodBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiRequestAwareProviderForWebFlux openApiRequestAwareProviderForWebFlux() {
        return new OpenApiRequestAwareProviderForWebFlux();
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForFlux defaultTypeResolverForFlux() {
        return new TypeResolverForFlux();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForFlux defaultInitialSchemaFactoryForFlux() {
        return new InitialSchemaBuilderForFlux();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForMono defaultInitialSchemaFactoryForMono() {
        return new InitialSchemaBuilderForMono();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiBaseUriSupplierForWebFlux openApiBaseUriProviderForWebFlux(OpenApiConfigurationProperties openApiConfigurationProperties) {
        return new OpenApiBaseUriSupplierForWebFlux(openApiConfigurationProperties);
    }
}
