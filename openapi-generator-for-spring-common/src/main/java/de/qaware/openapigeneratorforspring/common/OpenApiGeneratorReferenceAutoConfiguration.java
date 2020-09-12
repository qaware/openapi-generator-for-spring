package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceNameConflictResolverForSchema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameFactory defaultReferenceNameFactory() {
        return new DefaultReferenceNameFactory();
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
