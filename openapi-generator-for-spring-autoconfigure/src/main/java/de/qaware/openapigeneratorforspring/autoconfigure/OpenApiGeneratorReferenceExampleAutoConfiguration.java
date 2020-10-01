package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.example.DefaultReferenceIdentifierFactoryForExample;
import de.qaware.openapigeneratorforspring.common.reference.example.ReferenceDeciderForExample;
import de.qaware.openapigeneratorforspring.common.reference.example.ReferenceIdentifierConflictResolverForExample;
import de.qaware.openapigeneratorforspring.common.reference.example.ReferenceIdentifierFactoryForExample;
import de.qaware.openapigeneratorforspring.common.reference.example.ReferencedExamplesHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceExampleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedExamplesHandlerFactory referencedExamplesHandlerFactory(
            ReferenceDeciderForExample referenceDecider,
            ReferenceIdentifierFactoryForExample referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForExample referenceIdentifierConflictResolver
    ) {
        return new ReferencedExamplesHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierFactoryForExample defaultReferenceIdentifierFactoryForExample() {
        return new DefaultReferenceIdentifierFactoryForExample();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForExample defaultReferenceIdentifierConflictResolverForExample() {
        return new ReferenceIdentifierConflictResolverForExample() {
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
