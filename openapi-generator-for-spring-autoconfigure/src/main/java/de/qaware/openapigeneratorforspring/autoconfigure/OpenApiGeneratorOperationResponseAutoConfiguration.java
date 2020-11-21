package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.ApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseDefaultProvider;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseDefaultProvider;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponsesDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponsesFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesFromMethodCustomizer;
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
            List<OperationApiResponsesCustomizer> apiResponsesCustomizers
    ) {
        return new DefaultOperationResponseCustomizer(apiResponseAnnotationMapper, apiResponsesCustomizers);
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
            List<HandlerMethod.ResponseCodeMapper> handlerMethodResponseCodeMappers,
            ApiResponseDefaultProvider apiResponseDefaultProvider,
            SchemaResolver schemaResolver
    ) {
        return new DefaultOperationApiResponsesFromMethodCustomizer(handlerMethodResponseCodeMappers, apiResponseDefaultProvider, schemaResolver);
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
    public DefaultResponseCodeMapper defaultApiResponseCodeMapper() {
        return new DefaultResponseCodeMapper();
    }
}
