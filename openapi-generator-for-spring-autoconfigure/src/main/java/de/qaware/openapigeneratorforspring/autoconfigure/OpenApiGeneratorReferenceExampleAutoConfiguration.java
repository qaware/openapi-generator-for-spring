package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.example.DefaultReferenceIdentifierFactoryForExample;
import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferenceDeciderForExample;
import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferenceIdentifierConflictResolverForExample;
import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferenceIdentifierFactoryForExample;
import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferencedExamplesHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
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
    public ReferenceIdentifierConflictResolverForExample defaultReferenceIdentifierConflictResolverForExample(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForExample defaultReferenceDeciderForExample(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }

}
