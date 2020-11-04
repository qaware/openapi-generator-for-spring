package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodParameterTypeMapper;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodReturnTypeMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorWebAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new SpringWebHandlerMethodBuilder(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodParameterTypeMapper springWebHandlerMethodParameterTypeMapper() {
        return new SpringWebHandlerMethodParameterTypeMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodReturnTypeMapper springWebHandlerMethodReturnTypeMapper(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new SpringWebHandlerMethodReturnTypeMapper(annotationsSupplierFactory);
    }
}
