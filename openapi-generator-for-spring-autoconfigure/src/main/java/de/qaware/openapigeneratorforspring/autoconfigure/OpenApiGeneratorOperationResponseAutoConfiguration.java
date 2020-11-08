package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.*;
import de.qaware.openapigeneratorforspring.common.operation.response.*;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenApiGeneratorOperationResponseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationResponseCustomizer defaultOperationResponseCustomizer(
            ApiResponseAnnotationMapper apiResponseAnnotationMapper,
            List<OperationApiResponsesCustomizer> apiResponsesCustomizers,
            List<HandlerMethod.ReturnTypeMapper> handlerMethodReturnTypeMappers
    ) {
        return new DefaultOperationResponseCustomizer(apiResponseAnnotationMapper, apiResponsesCustomizers, handlerMethodReturnTypeMappers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiResponseAnnotationMapper defaultApiResponseAnnotationMapper(
            HeaderAnnotationMapper headerAnnotationMapper,
            ContentAnnotationMapper contentAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper,
            LinkAnnotationMapper linkAnnotationMapper
    ) {
        return new DefaultApiResponseAnnotationMapper(headerAnnotationMapper,
                contentAnnotationMapper, extensionAnnotationMapper, linkAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationApiResponsesFromMethodCustomizer defaultOperationApiResponsesFromMethodCustomizer(
            List<HandlerMethod.ApiResponseCodeMapper> handlerMethodApiResponseCodeMappers,
            ApiResponseDefaultProvider apiResponseDefaultProvider,
            SchemaResolver schemaResolver,
            List<HandlerMethod.ReturnTypeMapper> handlerMethodReturnTypeMappers
    ) {
        return new DefaultOperationApiResponsesFromMethodCustomizer(handlerMethodApiResponseCodeMappers, apiResponseDefaultProvider, schemaResolver, handlerMethodReturnTypeMappers);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationApiResponsesDescriptionCustomizer operationApiResponsesDescriptionCustomizer() {
        return new DefaultOperationApiResponsesDescriptionCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiResponseDefaultProvider defaultApiResponseDefaultProvider() {
        return new DefaultApiResponseDefaultProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultApiResponseCodeMapper defaultApiResponseCodeMapper() {
        return new DefaultApiResponseCodeMapper();
    }
}
