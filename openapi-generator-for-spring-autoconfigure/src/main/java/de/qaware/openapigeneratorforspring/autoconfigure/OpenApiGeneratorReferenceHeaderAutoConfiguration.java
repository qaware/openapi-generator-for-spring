package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.header.ReferenceDeciderForHeader;
import de.qaware.openapigeneratorforspring.common.reference.component.header.ReferenceIdentifierBuilderForHeader;
import de.qaware.openapigeneratorforspring.common.reference.component.header.ReferenceIdentifierConflictResolverForHeader;
import de.qaware.openapigeneratorforspring.common.reference.component.header.ReferencedHeadersHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierBuilderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceHeaderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedHeadersHandlerFactory referencedHeadersHandlerFactory(
            ReferenceDeciderForHeader referenceDecider,
            ReferenceIdentifierBuilderForHeader referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForHeader referenceIdentifierConflictResolver
    ) {
        return new ReferencedHeadersHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierBuilderForHeader defaultReferenceIdentifierFactoryForHeader(DefaultReferenceIdentifierBuilderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::buildIdentifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForHeader defaultReferenceIdentifierConflictResolverForHeader(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForHeader defaultReferenceDeciderForHeader(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }

}
