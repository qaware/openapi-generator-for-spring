package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromPathVariableAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromRequestHeaderAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromRequestParamAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.SpringWebOpenApiParameterBuilder;
import de.qaware.openapigeneratorforspring.common.paths.DefaultSpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodMapper;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodsMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.spring.SpringWebResponseEntityTypeResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorWebAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new DefaultSpringWebHandlerMethodBuilder(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.RequestBodyParameterMapper springWebHandSpringWebRequestBodyParameterMapper() {
        return new SpringWebHandlerMethodMapper.RequestBodyParameterMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.ReturnTypeMapper springWebHandlerMethodReturnTypeMapper(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new SpringWebHandlerMethodMapper.ReturnTypeMapper(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.ApiResponseCodeMapper defaultSpringWebApiResponseCodeMapper() {
        return new SpringWebHandlerMethodMapper.ApiResponseCodeMapper();
    }


    @Bean
    @ConditionalOnMissingBean
    public SpringWebOpenApiParameterBuilder springWebOpenApiParameterBuilder() {
        return new SpringWebOpenApiParameterBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromPathVariableAnnotation defaultParameterMethodConverterFromPathVariableAnnotation(SpringWebOpenApiParameterBuilder springWebOpenApiParameterBuilder) {
        return new DefaultParameterMethodConverterFromPathVariableAnnotation(springWebOpenApiParameterBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromRequestHeaderAnnotation defaultParameterMethodConverterFromRequestHeaderAnnotation(SpringWebOpenApiParameterBuilder springWebOpenApiParameterBuilder) {
        return new DefaultParameterMethodConverterFromRequestHeaderAnnotation(springWebOpenApiParameterBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromRequestParamAnnotation defaultParameterMethodConverterFromRequestParamAnnotation(SpringWebOpenApiParameterBuilder springWebOpenApiParameterBuilder) {
        return new DefaultParameterMethodConverterFromRequestParamAnnotation(springWebOpenApiParameterBuilder);
    }


    @Bean
    @ConditionalOnMissingBean
    public SpringWebResponseEntityTypeResolver defaultDefaultSpringResponseEntityTypeResolver() {
        return new SpringWebResponseEntityTypeResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebRequestMethodsMapper springWebRequestMethodMapper() {
        return new SpringWebRequestMethodsMapper();
    }
}
