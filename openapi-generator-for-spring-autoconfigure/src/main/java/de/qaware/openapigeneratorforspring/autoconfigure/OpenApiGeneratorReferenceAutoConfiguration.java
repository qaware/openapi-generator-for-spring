package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorReferenceCallbackAutoConfiguration.class,
        OpenApiGeneratorReferenceExampleAutoConfiguration.class,
        OpenApiGeneratorReferenceHeaderAutoConfiguration.class,
        OpenApiGeneratorReferenceLinkAutoConfiguration.class,
        OpenApiGeneratorReferenceParameterAutoConfiguration.class,
        OpenApiGeneratorReferenceRequestBodyAutoConfiguration.class,
        OpenApiGeneratorReferenceResponseAutoConfiguration.class,
        OpenApiGeneratorReferenceSchemaAutoConfiguration.class,
        OpenApiGeneratorReferenceSecuritySchemeAutoConfiguration.class,
        OpenApiGeneratorReferenceTagAutoConfiguration.class
})
public class OpenApiGeneratorReferenceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedItemSupportFactory referencedItemSupportFactory(List<ReferencedItemHandlerFactory> referencedItemHandlerFactories) {
        return new ReferencedItemSupportFactory(referencedItemHandlerFactories);
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
