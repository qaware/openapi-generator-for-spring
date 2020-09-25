package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.reference.requestbody.DefaultReferenceNameFactoryForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferenceDeciderForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferenceNameConflictResolverForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferenceNameFactoryForRequestBody;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferencedRequestBodyHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceRequestBodyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedRequestBodyHandlerFactory referencedRequestBodyHandlerFactory(
            ReferenceDeciderForRequestBody referenceDecider,
            ReferenceNameFactoryForRequestBody referenceNameFactory,
            ReferenceNameConflictResolverForRequestBody referenceNameConflictResolver
    ) {
        return new ReferencedRequestBodyHandlerFactory(referenceDecider, referenceNameFactory, referenceNameConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameFactoryForRequestBody defaultReferenceNameFactoryForRequestBody() {
        return new DefaultReferenceNameFactoryForRequestBody();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameConflictResolverForRequestBody defaultReferenceNameConflictResolverForRequestBody() {
        return new ReferenceNameConflictResolverForRequestBody() {
            // use default implementation
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForRequestBody defaultReferenceDeciderForRequestBody() {
        return new ReferenceDeciderForRequestBody() {
            // use default implementation
        };
    }

}
