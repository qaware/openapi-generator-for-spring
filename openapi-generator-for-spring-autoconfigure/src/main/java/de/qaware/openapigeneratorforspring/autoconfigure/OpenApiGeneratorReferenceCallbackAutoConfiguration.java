package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.callback.DefaultReferenceIdentifierFactoryForCallback;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferenceDeciderForCallback;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferenceIdentifierConflictResolverForCallback;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferenceIdentifierFactoryForCallback;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferencedCallbacksHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceCallbackAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedCallbacksHandlerFactory referencedCallbacksHandlerFactory(
            ReferenceDeciderForCallback referenceDecider,
            ReferenceIdentifierFactoryForCallback referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForCallback referenceIdentifierConflictResolver
    ) {
        return new ReferencedCallbacksHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierFactoryForCallback defaultReferenceIdentifierFactoryForCallback() {
        return new DefaultReferenceIdentifierFactoryForCallback();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForCallback defaultReferenceIdentifierConflictResolverForCallback(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForCallback defaultReferenceDeciderForCallback(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }

}
