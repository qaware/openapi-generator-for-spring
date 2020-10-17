package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.tag.ReferencedTagsHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceTagAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReferencedTagsHandlerFactory referencedTagsHandlerFactory() {
        return new ReferencedTagsHandlerFactory();
    }
}
