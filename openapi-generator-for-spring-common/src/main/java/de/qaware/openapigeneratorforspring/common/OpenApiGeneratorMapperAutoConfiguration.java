package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.mapper.ContactAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultContactAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultEncodingAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultHeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultInfoAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultLicenseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultLinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultLinkParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultServerVariableAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.EncodingAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.InfoAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LicenseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerVariableAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        OpenApiGeneratorSchemaMapperFactoryAutoConfiguration.class,
        OpenApiGeneratorSchemaAutoConfiguration.class,
        OpenApiGeneratorUtilAutoConfiguration.class,
})
public class OpenApiGeneratorMapperAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SchemaAnnotationMapper defaultSchemaAnnotationMapper(
            SchemaAnnotationMapperFactory schemaAnnotationMapperFactory,
            SchemaResolver schemaResolver
    ) {
        return schemaAnnotationMapperFactory.create(schemaResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public HeaderAnnotationMapper defaultHeaderAnnotationMapper(SchemaAnnotationMapper schemaAnnotationMapper) {
        return new DefaultHeaderAnnotationMapper(schemaAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public LinkAnnotationMapper defaultLinkAnnotationMapper(
            ParsableValueMapper parsableValueMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper,
            LinkParameterAnnotationMapper linkParameterAnnotationMapper,
            ServerAnnotationMapper serverAnnotationMapper
    ) {
        return new DefaultLinkAnnotationMapper(
                parsableValueMapper, extensionAnnotationMapper, linkParameterAnnotationMapper, serverAnnotationMapper
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public LinkParameterAnnotationMapper defaultLinkParameterAnnotationMapper() {
        return new DefaultLinkParameterAnnotationMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerAnnotationMapper defaultServerAnnotationMapper(ServerVariableAnnotationMapper serverVariableAnnotationMapper, ExtensionAnnotationMapper extensionAnnotationMapper) {
        return new DefaultServerAnnotationMapper(serverVariableAnnotationMapper, extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerVariableAnnotationMapper defaultServerVariableAnnotationMapper(ExtensionAnnotationMapper extensionAnnotationMapper) {
        return new DefaultServerVariableAnnotationMapper(extensionAnnotationMapper);
    }


    @Bean
    @ConditionalOnMissingBean
    public ContentAnnotationMapper defaultContentAnnotationMapper(
            EncodingAnnotationMapper encodingAnnotationMapper,
            SchemaAnnotationMapper schemaAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper,
            ExampleObjectAnnotationMapper exampleObjectAnnotationMapper
    ) {
        return new DefaultContentAnnotationMapper(
                encodingAnnotationMapper, schemaAnnotationMapper,
                extensionAnnotationMapper, exampleObjectAnnotationMapper
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public ExampleObjectAnnotationMapper defaultExampleObjectAnnotationMapper(
            ParsableValueMapper parsableValueMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultExampleObjectAnnotationMapper(parsableValueMapper, extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public EncodingAnnotationMapper defaultEncodingAnnotationMapper(
            HeaderAnnotationMapper headerAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultEncodingAnnotationMapper(headerAnnotationMapper, extensionAnnotationMapper);
    }


    @Bean
    @ConditionalOnMissingBean
    public InfoAnnotationMapper defaultInfoAnnotationMapper(
            ContactAnnotationMapper contactAnnotationMapper,
            LicenseAnnotationMapper licenseAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultInfoAnnotationMapper(contactAnnotationMapper, licenseAnnotationMapper, extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ContactAnnotationMapper defaultContactAnnotationMapper(
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultContactAnnotationMapper(extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public LicenseAnnotationMapper defaultLicenseAnnotationMapper(
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultLicenseAnnotationMapper(extensionAnnotationMapper);
    }
}
