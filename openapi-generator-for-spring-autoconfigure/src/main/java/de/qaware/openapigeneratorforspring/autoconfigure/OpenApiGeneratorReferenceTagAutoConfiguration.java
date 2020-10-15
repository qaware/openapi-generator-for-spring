package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.tag.ReferencedTagsHandlerFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationAnnotationsSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceTagAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReferencedTagsHandlerFactory referencedTagsHandlerFactory(
            OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier,
            TagAnnotationMapper tagAnnotationMapper
    ) {
        return new ReferencedTagsHandlerFactory(springBootApplicationAnnotationsSupplier, tagAnnotationMapper);
    }
}
