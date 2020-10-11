package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.DefaultReferenceIdentifierFactoryForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferenceDeciderForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferenceIdentifierConflictResolverForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferenceIdentifierFactoryForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferencedRequestBodyHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceRequestBodyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedRequestBodyHandlerFactory referencedRequestBodyHandlerFactory(
            ReferenceDeciderForRequestBody referenceDecider,
            ReferenceIdentifierFactoryForRequestBody referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForRequestBody referenceIdentifierConflictResolver
    ) {
        return new ReferencedRequestBodyHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierFactoryForRequestBody defaultReferenceIdentifierFactoryForRequestBody() {
        return new DefaultReferenceIdentifierFactoryForRequestBody();
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
