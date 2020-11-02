package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.DefaultOpenApiCustomizer;
import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiCustomizer;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.supplier.DefaultOpenApiDefaultServerSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

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
@EnableConfigurationProperties(OpenApiConfigurationProperties.class)
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
            ExtensionAnnotationMapper extensionAnnotationMapper,
            ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper,
            List<OpenApiServersSupplier> openApiServersSuppliers,
            OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier,
            OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier
    ) {
        return new DefaultOpenApiCustomizer(
                serverAnnotationMapper,
                externalDocumentationAnnotationMapper,
                extensionAnnotationMapper,
                openApiInfoSupplier,
                openApiServersSuppliers,
                springBootApplicationAnnotationsSupplier,
                openAPIDefinitionAnnotationSupplier
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiServersSupplier defaultOpenApiDefaultServerSupplier(
            OpenApiBaseUriSupplier openApiBaseUriSupplier, // provided by WebMVC or WebFlux
            OpenApiConfigurationProperties properties
    ) {
        return new DefaultOpenApiDefaultServerSupplier(openApiBaseUriSupplier, properties);
    }
}
