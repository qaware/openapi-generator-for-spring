package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.reference.example.DefaultReferenceNameFactoryForExample;
import de.qaware.openapigeneratorforspring.common.reference.example.ReferenceDeciderForExample;
import de.qaware.openapigeneratorforspring.common.reference.example.ReferenceNameConflictResolverForExample;
import de.qaware.openapigeneratorforspring.common.reference.example.ReferenceNameFactoryForExample;
import de.qaware.openapigeneratorforspring.common.reference.example.ReferencedExamplesHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceExampleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedExamplesHandlerFactory referencedExamplesHandlerFactory(
            ReferenceDeciderForExample referenceDecider,
            ReferenceNameFactoryForExample referenceNameFactory,
            ReferenceNameConflictResolverForExample referenceNameConflictResolver
    ) {
        return new ReferencedExamplesHandlerFactory(referenceDecider, referenceNameFactory, referenceNameConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameFactoryForExample defaultReferenceNameFactoryForExample() {
        return new DefaultReferenceNameFactoryForExample();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameConflictResolverForExample defaultReferenceNameConflictResolverForExample() {
        return new ReferenceNameConflictResolverForExample() {
            // use default implementation
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForExample defaultReferenceDeciderForExample() {
        return new ReferenceDeciderForExample() {
            // use default implementation
        };
    }

}
