package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseDefaultProvider;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseDefaultProvider;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponsesDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponsesFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.DefaultReferenceIdentifierFactoryForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceDeciderForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceIdentifierConflictResolverForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceIdentifierFactoryForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferencedApiResponsesHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorAnnotationAutoConfiguration.class,
        OpenApiGeneratorMapperAutoConfiguration.class
})
public class OpenApiGeneratorOperationResponseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationResponseCustomizer defaultOperationResponseCustomizer(
            ApiResponseAnnotationMapper apiResponseAnnotationMapper,
            List<OperationApiResponsesCustomizer> apiResponsesCustomizers,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOperationResponseCustomizer(apiResponseAnnotationMapper, apiResponsesCustomizers, annotationsSupplierFactory);
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
            ApiResponseCodeMapper apiResponseCodeMapper,
            ApiResponseDefaultProvider apiResponseDefaultProvider,
            SchemaResolver schemaResolver,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOperationApiResponsesFromMethodCustomizer(apiResponseCodeMapper, apiResponseDefaultProvider,
                schemaResolver, annotationsSupplierFactory);
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
    public ApiResponseCodeMapper defaultApiResponseCodeMapper(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new DefaultApiResponseCodeMapper(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferencedApiResponsesHandlerFactory referencedApiResponsesHandlerFactory(
            ReferenceDeciderForApiResponse referenceDecider,
            ReferenceIdentifierFactoryForApiResponse referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForApiResponse referenceIdentifierConflictResolver
    ) {
        return new ReferencedApiResponsesHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierFactoryForApiResponse defaultReferenceIdentifierFactoryForApiResponse() {
        return new DefaultReferenceIdentifierFactoryForApiResponse();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForApiResponse defaultReferenceIdentifierConflictResolverForApiResponse(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForApiResponse defaultReferenceDeciderForApiResponse(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }
}
