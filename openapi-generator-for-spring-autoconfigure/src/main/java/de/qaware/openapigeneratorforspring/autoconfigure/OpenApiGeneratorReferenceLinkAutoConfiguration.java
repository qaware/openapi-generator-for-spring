package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferenceDeciderForLink;
import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferenceIdentifierBuilderForLink;
import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferenceIdentifierConflictResolverForLink;
import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferencedLinksHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierBuilderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceLinkAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedLinksHandlerFactory referencedLinksHandlerFactory(
            ReferenceDeciderForLink referenceDecider,
            ReferenceIdentifierBuilderForLink referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForLink referenceIdentifierConflictResolver
    ) {
        return new ReferencedLinksHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierBuilderForLink defaultReferenceIdentifierFactoryForLink(DefaultReferenceIdentifierBuilderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::buildIdentifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForLink defaultReferenceIdentifierConflictResolverForLink(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForLink defaultReferenceDeciderForLink(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }

}
