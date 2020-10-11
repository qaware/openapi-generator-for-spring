package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import de.qaware.openapigeneratorforspring.common.reference.header.DefaultReferenceIdentifierFactoryForHeader;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferenceDeciderForHeader;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferenceIdentifierConflictResolverForHeader;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferenceIdentifierFactoryForHeader;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferencedHeadersHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceHeaderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedHeadersHandlerFactory referencedHeadersHandlerFactory(
            ReferenceDeciderForHeader referenceDecider,
            ReferenceIdentifierFactoryForHeader referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForHeader referenceIdentifierConflictResolver
    ) {
        return new ReferencedHeadersHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierFactoryForHeader defaultReferenceIdentifierFactoryForHeader() {
        return new DefaultReferenceIdentifierFactoryForHeader();
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
