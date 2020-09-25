package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.DefaultExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultHeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.DefaultSchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * This extra auto-configuration resolves the dependency loop between
 * {@link DefaultSchemaResolver}, {@link SchemaAnnotationMapper} and {@link
 * DefaultHeaderAnnotationMapper} using {@link SchemaAnnotationMapperFactory}.
 */
@Import(OpenApiGeneratorUtilAutoConfiguration.class)
public class OpenApiGeneratorSchemaMapperFactoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ParsableValueMapper defaultParsableValueMapper(OpenApiObjectMapperSupplier objectMapperSupplier) {
        return new DefaultParsableValueMapper(objectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExtensionAnnotationMapper defaultExtensionAnnotationMapper(ParsableValueMapper parsableValueMapper) {
        return new DefaultExtensionAnnotationMapper(parsableValueMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExternalDocumentationAnnotationMapper defaultExternalDocumentationAnnotationMapper(ExtensionAnnotationMapper extensionAnnotationMapper) {
        return new DefaultExternalDocumentationAnnotationMapper(extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaAnnotationMapperFactory defaultSchemaAnnotationMapperFactory(
            ParsableValueMapper parsableValueMapper,
            ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultSchemaAnnotationMapperFactory(parsableValueMapper, externalDocumentationAnnotationMapper, extensionAnnotationMapper);
    }


}
