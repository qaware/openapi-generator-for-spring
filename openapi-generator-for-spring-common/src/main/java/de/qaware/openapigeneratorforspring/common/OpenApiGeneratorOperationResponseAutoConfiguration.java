package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponsesFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.DefaultReferenceNameFactoryForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceDeciderForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceNameConflictResolverForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceNameFactoryForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferencedApiResponsesHandlerFactory;
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
            ApiResponseCodeMapper apiResponseCodeMapper,
            HeaderAnnotationMapper headerAnnotationMapper,
            ContentAnnotationMapper contentAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper,
            LinkAnnotationMapper linkAnnotationMapper
    ) {
        return new DefaultApiResponseAnnotationMapper(apiResponseCodeMapper, headerAnnotationMapper,
                contentAnnotationMapper, extensionAnnotationMapper, linkAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationApiResponsesFromMethodCustomizer defaultOperationApiResponseFromMethodCustomizer(
            DefaultApiResponseCodeMapper defaultApiResponseCodeMapper, SchemaResolver schemaResolver,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOperationApiResponsesFromMethodCustomizer(defaultApiResponseCodeMapper, schemaResolver, annotationsSupplierFactory);
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
            ReferenceNameFactoryForApiResponse referenceNameFactory,
            ReferenceNameConflictResolverForApiResponse referenceNameConflictResolver
    ) {
        return new ReferencedApiResponsesHandlerFactory(referenceDecider, referenceNameFactory, referenceNameConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameFactoryForApiResponse defaultReferenceNameFactoryForApiResponse() {
        return new DefaultReferenceNameFactoryForApiResponse();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameConflictResolverForApiResponse defaultReferenceNameConflictResolverForApiResponse() {
        return new ReferenceNameConflictResolverForApiResponse() {
            // use default implementation
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForApiResponse defaultReferenceDeciderForApiResponse() {
        return new ReferenceDeciderForApiResponse() {
            // use default implementation
        };
    }
}
