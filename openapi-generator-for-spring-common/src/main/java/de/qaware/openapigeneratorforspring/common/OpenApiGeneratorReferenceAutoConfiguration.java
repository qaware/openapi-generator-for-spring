package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
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
    ReferenceNameConflictResolver defaultReferenceNameConflictResolver() {
        return new DefaultReferenceNameConflictResolver();
    }
}
