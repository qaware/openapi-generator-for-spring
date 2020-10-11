package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferenceIdentifierFactoryForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceIdentifierConflictResolverForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceIdentifierFactoryForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorReferenceExampleAutoConfiguration.class,
        OpenApiGeneratorReferenceHeaderAutoConfiguration.class,
        OpenApiGeneratorReferenceRequestBodyAutoConfiguration.class,
})
public class OpenApiGeneratorReferenceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedItemSupportFactory referencedItemSupportFactory(List<ReferencedItemHandlerFactory<?, ?>> referencedItemHandlerFactories) {
        return new ReferencedItemSupportFactory(referencedItemHandlerFactories);
    }

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

    @Bean
    @ConditionalOnMissingBean
    public DefaultReferenceIdentifierConflictResolverFactory defaultReferenceIdentifierConflictResolverForTypeFactory() {
        return new DefaultReferenceIdentifierConflictResolverFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultReferenceDeciderFactory defaultReferenceDeciderFactory() {
        return new DefaultReferenceDeciderFactory();
    }

}
