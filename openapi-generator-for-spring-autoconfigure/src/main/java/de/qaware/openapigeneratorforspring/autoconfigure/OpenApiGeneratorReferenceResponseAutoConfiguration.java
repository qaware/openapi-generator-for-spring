package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.operation.response.reference.DefaultReferenceIdentifierFactoryForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceDeciderForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceIdentifierConflictResolverForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceIdentifierFactoryForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferencedApiResponsesHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceResponseAutoConfiguration {

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
