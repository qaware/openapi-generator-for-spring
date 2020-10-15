package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.response.DefaultReferenceIdentifierFactoryForApiResponse;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferenceDeciderForApiResponse;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferenceIdentifierConflictResolverForApiResponse;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferenceIdentifierFactoryForApiResponse;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferencedApiResponsesHandlerFactory;
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
