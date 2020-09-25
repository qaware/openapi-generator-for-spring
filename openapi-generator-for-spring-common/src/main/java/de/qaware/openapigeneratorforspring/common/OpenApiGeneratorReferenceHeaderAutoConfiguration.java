package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.reference.header.DefaultReferenceNameFactoryForHeader;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferenceDeciderForHeader;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferenceNameConflictResolverForHeader;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferenceNameFactoryForHeader;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferencedHeadersHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceHeaderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedHeadersHandlerFactory referencedHeadersHandlerFactory(
            ReferenceDeciderForHeader referenceDecider,
            ReferenceNameFactoryForHeader referenceNameFactory,
            ReferenceNameConflictResolverForHeader referenceNameConflictResolver
    ) {
        return new ReferencedHeadersHandlerFactory(referenceDecider, referenceNameFactory, referenceNameConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameFactoryForHeader defaultReferenceNameFactoryForHeader() {
        return new DefaultReferenceNameFactoryForHeader();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameConflictResolverForHeader defaultReferenceNameConflictResolverForHeader() {
        return new ReferenceNameConflictResolverForHeader() {
            // use default implementation
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForHeader defaultReferenceDeciderForHeader() {
        return new ReferenceDeciderForHeader() {
            // use default implementation
        };
    }

}
