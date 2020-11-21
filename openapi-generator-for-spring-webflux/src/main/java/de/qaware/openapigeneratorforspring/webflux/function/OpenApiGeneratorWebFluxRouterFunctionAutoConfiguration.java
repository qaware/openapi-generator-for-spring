package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorWebFluxRouterFunctionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public RouterFunctionHandlerMethodWithInfoBuilder routerFunctionHandlerMethodWithInfoBuilder(
            AnnotationsSupplierFactory annotationsSupplierFactory,
            ConfigurableListableBeanFactory beanFactory
    ) {
        return new RouterFunctionHandlerMethodWithInfoBuilder(annotationsSupplierFactory, beanFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterFunctionHandlerMethodMapper.RequestBodyMapper routerFunctionRequestBodyParameterMapper() {
        return new RouterFunctionHandlerMethodMapper.RequestBodyMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterFunctionHandlerMethodMapper.ResponseMapper routerFunctionReturnTypeMapper() {
        return new RouterFunctionHandlerMethodMapper.ResponseMapper();
    }


    @Bean
    @ConditionalOnMissingBean
    public RouterFunctionParameterMethodConverter routerFunctionParameterMethodConverter() {
        return new RouterFunctionParameterMethodConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForRouterFunctionReturnType initialSchemaBuilderForRouterFunctionReturnType(
            SchemaAnnotationMapperFactory schemaAnnotationMapperFactory
    ) {
        return new InitialSchemaBuilderForRouterFunctionReturnType(schemaAnnotationMapperFactory);
    }

}
