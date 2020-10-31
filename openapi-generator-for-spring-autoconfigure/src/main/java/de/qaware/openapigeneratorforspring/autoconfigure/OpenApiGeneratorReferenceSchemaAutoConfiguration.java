package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.schema.DefaultReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.DefaultReferenceIdentifierBuilderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceIdentifierBuilderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceIdentifierConflictResolverForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceSchemaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedSchemaHandlerFactory referencedSchemaHandlerFactory(
            ReferenceDeciderForSchema referenceDecider,
            ReferenceIdentifierBuilderForSchema referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForSchema referenceIdentifierConflictResolver
    ) {
        return new ReferencedSchemaHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierBuilderForSchema defaultReferenceIdentifierFactoryForSchema() {
        return new DefaultReferenceIdentifierBuilderForSchema();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForSchema defaultReferenceIdentifierConflictResolverForSchema(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defImpl -> defImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForSchema defaultReferenceDeciderForSchema() {
        return new DefaultReferenceDeciderForSchema();
    }
}
