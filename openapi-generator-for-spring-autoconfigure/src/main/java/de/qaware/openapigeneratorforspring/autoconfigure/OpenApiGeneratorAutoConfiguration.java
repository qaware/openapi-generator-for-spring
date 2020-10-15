package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.DefaultOpenApiCustomizer;
import de.qaware.openapigeneratorforspring.common.OpenApiCustomizer;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.security.OpenApiSecuritySchemesSupplier;
import de.qaware.openapigeneratorforspring.common.server.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationAnnotationsSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@Import({
        OpenApiGeneratorAnnotationAutoConfiguration.class,
        OpenApiGeneratorFilterAutoConfiguration.class,
        OpenApiGeneratorInfoAutoConfiguration.class,
        OpenApiGeneratorMapperAutoConfiguration.class,
        OpenApiGeneratorOperationAutoConfiguration.class,
        OpenApiGeneratorPathsAutoConfiguration.class,
        OpenApiGeneratorReferenceAutoConfiguration.class,
        OpenApiGeneratorSchemaAutoConfiguration.class,
        OpenApiGeneratorUtilAutoConfiguration.class,
})
public class OpenApiGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiGenerator openApiGenerator(
            PathsBuilder pathsBuilder,
            ReferencedItemSupportFactory referencedItemSupportFactory,
            List<OpenApiCustomizer> openApiCustomizers
    ) {
        return new OpenApiGenerator(pathsBuilder, referencedItemSupportFactory, openApiCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOpenApiCustomizer defaultOpenApiCustomizer(
            OpenApiInfoSupplier openApiInfoSupplier,
            ServerAnnotationMapper serverAnnotationMapper,
            Optional<List<OpenApiServersSupplier>> optionalOpenApiServersSuppliers,
            SecuritySchemeAnnotationMapper securitySchemeAnnotationMapper,
            Optional<List<OpenApiSecuritySchemesSupplier>> optionalOpenApiSecuritySchemesSupplier,
            OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier

    ) {
        return new DefaultOpenApiCustomizer(openApiInfoSupplier,
                serverAnnotationMapper,
                optionalOpenApiServersSuppliers.orElseGet(Collections::emptyList),
                securitySchemeAnnotationMapper,
                optionalOpenApiSecuritySchemesSupplier.orElseGet(Collections::emptyList),
                springBootApplicationAnnotationsSupplier
        );
    }
}
