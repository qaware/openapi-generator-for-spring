package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferenceDeciderForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferenceIdentifierBuilderForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferenceIdentifierConflictResolverForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferencedRequestBodyHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierBuilderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceRequestBodyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedRequestBodyHandlerFactory referencedRequestBodyHandlerFactory(
            ReferenceDeciderForRequestBody referenceDecider,
            ReferenceIdentifierBuilderForRequestBody referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForRequestBody referenceIdentifierConflictResolver
    ) {
        return new ReferencedRequestBodyHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierBuilderForRequestBody defaultReferenceIdentifierFactoryForRequestBody(DefaultReferenceIdentifierBuilderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::buildIdentifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForRequestBody defaultReferenceIdentifierConflictResolverForRequestBody(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForRequestBody defaultReferenceDeciderForRequestBody(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }

}
