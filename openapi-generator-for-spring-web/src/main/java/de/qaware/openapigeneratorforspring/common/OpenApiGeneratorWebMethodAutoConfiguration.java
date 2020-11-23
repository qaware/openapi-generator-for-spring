package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodReturnTypeMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(OpenApiGeneratorWebMethodMergerAutoConfiguration.class)
public class OpenApiGeneratorWebMethodAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.ContextModifierMapper springWebHandlerMethodContextAwareMapper(SpringWebHandlerMethodContentTypesMapper contentTypesMapper) {
        return new SpringWebHandlerMethodMapper.ContextModifierMapper(contentTypesMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.RequestBodyMapper springWebHandlerMethodRequestBodyMapper(
            SpringWebHandlerMethodContentTypesMapper springWebHandlerMethodContentTypesMapper,
            SpringWebHandlerMethodRequestBodyParameterMapper springWebHandlerMethodRequestBodyParameterMapper
    ) {
        return new SpringWebHandlerMethodMapper.RequestBodyMapper(springWebHandlerMethodContentTypesMapper, springWebHandlerMethodRequestBodyParameterMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.ResponseMapper springWebHandlerMethodResponseMapper(
            SpringWebHandlerMethodContentTypesMapper springWebHandlerMethodContentTypesMapper,
            SpringWebHandlerMethodResponseCodeMapper springWebHandlerMethodResponseCodeMapper,
            SpringWebHandlerMethodReturnTypeMapper springWebHandlerMethodReturnTypeMapper
    ) {
        return new SpringWebHandlerMethodMapper.ResponseMapper(springWebHandlerMethodContentTypesMapper, springWebHandlerMethodResponseCodeMapper, springWebHandlerMethodReturnTypeMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodRequestBodyParameterMapper springWebHandlerMethodRequestBodyParameterMapper() {
        return new SpringWebHandlerMethodRequestBodyParameterMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodReturnTypeMapper springWebHandlerMethodReturnTypeMapper(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new SpringWebHandlerMethodReturnTypeMapper(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodResponseCodeMapper springWebHandlerMethodResponseCodeMapper() {
        return new SpringWebHandlerMethodResponseCodeMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodContentTypesMapper springWebHandlerMethodContentTypesMapper() {
        return new SpringWebHandlerMethodContentTypesMapper();
    }
}
