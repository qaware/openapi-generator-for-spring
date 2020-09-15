package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferenceNameFactoryForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceNameConflictResolverForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceNameFactoryForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(OpenApiGeneratorReferenceExampleAutoConfiguration.class)
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
            ReferenceNameFactoryForSchema referenceNameFactory,
            ReferenceNameConflictResolverForSchema referenceNameConflictResolver
    ) {
        return new ReferencedSchemaHandlerFactory(referenceDecider, referenceNameFactory, referenceNameConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameFactoryForSchema defaultReferenceNameFactoryForSchema() {
        return new DefaultReferenceNameFactoryForSchema();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameConflictResolverForSchema defaultReferenceNameConflictResolverForSchema() {
        return new ReferenceNameConflictResolverForSchema() {
            // use default implementation
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForSchema defaultReferenceDeciderForSchema() {
        return new DefaultReferenceDeciderForSchema();
    }

}
