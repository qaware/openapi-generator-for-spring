package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.DefaultReferenceIdentifierFactoryForParameter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferenceDeciderForParameter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferenceIdentifierConflictResolverForParameter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferenceIdentifierFactoryForParameter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferencedParametersHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceParameterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedParametersHandlerFactory referencedParametersHandlerFactory(
            ReferenceIdentifierFactoryForParameter referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForParameter referenceIdentifierConflictResolver,
            ReferenceDeciderForParameter referenceDecider
    ) {
        return new ReferencedParametersHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierFactoryForParameter defaultReferenceIdentifierFactoryForParameter() {
        return new DefaultReferenceIdentifierFactoryForParameter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForParameter defaultReferenceIdentifierConflictResolverForParameter(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForParameter defaultReferenceDeciderForParameter(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }
}
