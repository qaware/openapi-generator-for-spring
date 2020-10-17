package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.tag.ReferencedTagsHandlerFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenAPIDefinitionAnnotationSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceTagAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReferencedTagsHandlerFactory referencedTagsHandlerFactory(
            OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier,
            TagAnnotationMapper tagAnnotationMapper
    ) {
        return new ReferencedTagsHandlerFactory(
                openAPIDefinitionAnnotationSupplier,
                tagAnnotationMapper
        );
    }
}
