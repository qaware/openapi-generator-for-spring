package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceNameConflictResolverForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenApiGeneratorReferenceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReferencedItemSupportFactory referencedItemSupportFactory(List<ReferencedItemHandlerFactory<?>> referencedItemHandlerFactories) {
        return new ReferencedItemSupportFactory(referencedItemHandlerFactories);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameFactory defaultReferenceNameFactory() {
        return new DefaultReferenceNameFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferencedSchemaHandlerFactory referencedSchemaHandlerFactory(
            ReferenceNameFactory referenceNameFactory,
            ReferenceNameConflictResolverForSchema referenceNameConflictResolver,
            ReferenceDeciderForSchema referenceDecider
    ) {
        return new ReferencedSchemaHandlerFactory(referenceNameFactory, referenceNameConflictResolver, referenceDecider);
    }

    @Bean
    @ConditionalOnMissingBean
    ReferenceNameConflictResolverForSchema defaultReferenceNameConflictResolverForSchema() {
        return new ReferenceNameConflictResolverForSchema() {
        };
    }

    @Bean
    @ConditionalOnMissingBean
    ReferenceDeciderForSchema defaultReferenceDeciderForSchema() {
        return new DefaultReferenceDeciderForSchema();
    }

}
