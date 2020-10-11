package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.DefaultOpenApiCustomizer;
import de.qaware.openapigeneratorforspring.common.OpenApiCustomizer;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.server.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.tags.TagsSupportFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationClassSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@Import({
        OpenApiGeneratorReferenceAutoConfiguration.class,
        OpenApiGeneratorPathsAutoConfiguration.class,
        OpenApiGeneratorUtilAutoConfiguration.class,
        OpenApiGeneratorInfoAutoConfiguration.class,
        OpenApiGeneratorTagsAutoConfiguration.class
})
public class OpenApiGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiGenerator openApiGenerator(
            PathsBuilder pathsBuilder,
            ReferencedItemSupportFactory referencedItemSupportFactory,
            TagsSupportFactory tagsSupportFactory,
            List<OpenApiCustomizer> openApiCustomizers,
            OpenApiSpringBootApplicationClassSupplier springBootApplicationClassSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new OpenApiGenerator(
                pathsBuilder,
                referencedItemSupportFactory,
                tagsSupportFactory,
                openApiCustomizers,
                springBootApplicationClassSupplier,
                annotationsSupplierFactory

        );
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOpenApiCustomizer defaultOpenApiCustomizer(OpenApiInfoSupplier openApiInfoSupplier, ServerAnnotationMapper serverAnnotationMapper, Optional<List<OpenApiServersSupplier>> optionalOpenApiServersSuppliers) {
        return new DefaultOpenApiCustomizer(openApiInfoSupplier, serverAnnotationMapper, optionalOpenApiServersSuppliers.orElseGet(Collections::emptyList));
    }
}
