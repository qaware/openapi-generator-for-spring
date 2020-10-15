package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.CallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ContactAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultCallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultContactAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultEncodingAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultHeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultInfoAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultLicenseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultLinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultLinkParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultOAuthFlowAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultOAuthFlowsAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultOperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultRequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultSecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultServerVariableAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultTagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.EncodingAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.InfoAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LicenseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.OAuthFlowAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.OAuthFlowsAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerVariableAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.parameter.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.DefaultSchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

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
    public SchemaAnnotationMapperFactory defaultSchemaAnnotationMapperFactory(
            ParsableValueMapper parsableValueMapper,
            ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultSchemaAnnotationMapperFactory(parsableValueMapper, externalDocumentationAnnotationMapper, extensionAnnotationMapper);
    }

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
    public OperationAnnotationMapper defaultOperationAnnotationMapper(
            RequestBodyAnnotationMapper requestBodyAnnotationMapper,
            ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper,
            ParameterAnnotationMapper parameterAnnotationMapper,
            ApiResponseAnnotationMapper apiResponseAnnotationMapper,
            ServerAnnotationMapper serverAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper) {
        return new DefaultOperationAnnotationMapper(requestBodyAnnotationMapper, externalDocumentationAnnotationMapper,
                parameterAnnotationMapper, apiResponseAnnotationMapper, serverAnnotationMapper, extensionAnnotationMapper);
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

    @Bean
    @ConditionalOnMissingBean
    public RequestBodyAnnotationMapper defaultRequestBodyAnnotationMapper(
            ContentAnnotationMapper contentAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultRequestBodyAnnotationMapper(contentAnnotationMapper, extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public TagAnnotationMapper defaultTagAnnotationMapper(
            ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultTagAnnotationMapper(externalDocumentationAnnotationMapper, extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public CallbackAnnotationMapper defaultCallbackAnnotationMapper(OperationAnnotationMapper operationAnnotationMapper) {
        return new DefaultCallbackAnnotationMapper(operationAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecuritySchemeAnnotationMapper defaultSecurityAnnotationMapper(OAuthFlowsAnnotationMapper oAuthFlowsAnnotationMapper, ExtensionAnnotationMapper extensionAnnotationMapper) {
        return new DefaultSecuritySchemeAnnotationMapper(oAuthFlowsAnnotationMapper, extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuthFlowsAnnotationMapper defaultOAuthFlowsAnnotationMapper(OAuthFlowAnnotationMapper oAuthFlowAnnotationMapper, ExtensionAnnotationMapper extensionAnnotationMapper) {
        return new DefaultOAuthFlowsAnnotationMapper(oAuthFlowAnnotationMapper, extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuthFlowAnnotationMapper defaultOAuthFlowAnnotationMapper(ExtensionAnnotationMapper extensionAnnotationMapper) {
        return new DefaultOAuthFlowAnnotationMapper(extensionAnnotationMapper);
    }
}
