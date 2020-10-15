package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.schema.DefaultReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.DefaultReferenceIdentifierFactoryForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceIdentifierConflictResolverForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceIdentifierFactoryForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceSchemaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedSchemaHandlerFactory referencedSchemaHandlerFactory(
            ReferenceDeciderForSchema referenceDecider,
            ReferenceIdentifierFactoryForSchema referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForSchema referenceIdentifierConflictResolver
    ) {
        return new ReferencedSchemaHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierFactoryForSchema defaultReferenceIdentifierFactoryForSchema() {
        return new DefaultReferenceIdentifierFactoryForSchema();
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
