package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.filter.operation.ExcludeHiddenOperationFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.NoOperationsPathItemFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.PathItemFilter;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultEncodingAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultHeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultLinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultLinkParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultServerVariableAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.EncodingAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerVariableAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultDeprecatedOperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationExternalDocsCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationIdCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationServersCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationSummaryAndDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationTagsCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdProvider;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.MethodResponseApiResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.DefaultSchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import io.swagger.v3.core.util.Json;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
public class OpenApiGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResource openApiResource(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier openApiObjectMapperSupplier) {
        return new OpenApiResource(openApiGenerator, openApiObjectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiGenerator openApiGenerator(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            OperationBuilder operationBuilder,
            List<PathItemFilter> pathItemFilters,
            List<OperationFilter> operationFilters,
            OperationIdConflictResolver operationIdConflictResolver,
            ReferenceNameFactory referenceNameFactory,
            ReferenceNameConflictResolver referenceNameConflictResolver
    ) {
        return new OpenApiGenerator(
                requestMappingHandlerMapping,
                operationBuilder,
                pathItemFilters,
                operationFilters,
                operationIdConflictResolver,
                referenceNameFactory,
                referenceNameConflictResolver
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationBuilder operationBuilder(OperationIdProvider operationIdProvider, List<OperationCustomizer> operationCustomizers) {
        return new OperationBuilder(operationIdProvider, operationCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationIdProvider defaultOperationIdProvider() {
        return new DefaultOperationIdProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationIdConflictResolver defaultOperationIdConflictResolver() {
        return new DefaultOperationIdConflictResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultDeprecatedOperationCustomizer defaultDeprecatedOperationCustomizer() {
        return new DefaultDeprecatedOperationCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationSummaryAndDescriptionCustomizer defaultOperationSummaryAndDescriptionCustomizer() {
        return new DefaultOperationSummaryAndDescriptionCustomizer();
    }


    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationTagsCustomizer defaultOperationTagsCustomizer() {
        return new DefaultOperationTagsCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationExternalDocsCustomizer defaultOperationExternalDocsCustomizer(ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper) {
        return new DefaultOperationExternalDocsCustomizer(externalDocumentationAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationServersCustomizer defaultOperationServersCustomizer(ServerAnnotationMapper serverAnnotationMapper) {
        return new DefaultOperationServersCustomizer(serverAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationIdCustomizer defaultOperationIdCustomizer(OperationIdProvider operationIdProvider) {
        return new DefaultOperationIdCustomizer(operationIdProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationResponseCustomizer defaultOperationResponseCustomizer(
            HeaderAnnotationMapper headerAnnotationMapper,
            ContentAnnotationMapper contentAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper,
            LinkAnnotationMapper linkAnnotationMapper,
            ApiResponseCodeMapper apiResponseCodeMapper,
            List<OperationApiResponseCustomizer> apiResponsesCustomizers
    ) {
        return new DefaultOperationResponseCustomizer(
                headerAnnotationMapper, contentAnnotationMapper,
                extensionAnnotationMapper, linkAnnotationMapper,
                apiResponseCodeMapper, apiResponsesCustomizers
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodResponseApiResponseCustomizer methodResponseApiResponseCustomizer(DefaultApiResponseCodeMapper defaultApiResponseCodeMapper) {
        return new MethodResponseApiResponseCustomizer(defaultApiResponseCodeMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultApiResponseCodeMapper defaultApiResponseCodeMapper() {
        return new DefaultApiResponseCodeMapper();
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
    public ExternalDocumentationAnnotationMapper defaultExternalDocumentationAnnotationMapper(ExtensionAnnotationMapper extensionAnnotationMapper) {
        return new DefaultExternalDocumentationAnnotationMapper(extensionAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public LinkParameterAnnotationMapper defaultLinkParameterAnnotationMapper() {
        return new DefaultLinkParameterAnnotationMapper();
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
    public HeaderAnnotationMapper defaultHeaderAnnotationMapper(SchemaAnnotationMapper schemaAnnotationMapper) {
        return new DefaultHeaderAnnotationMapper(schemaAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaAnnotationMapper defaultSchemaAnnotationMapper(
            ParsableValueMapper parsableValueMapper,
            ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper,
            SchemaResolver schemaResolver, ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultSchemaAnnotationMapper(parsableValueMapper, externalDocumentationAnnotationMapper,
                schemaResolver, extensionAnnotationMapper);
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
    public NoOperationsPathItemFilter noOperationsPathItemFilter() {
        return new NoOperationsPathItemFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcludeHiddenOperationFilter excludeHiddenOperationFilter() {
        return new ExcludeHiddenOperationFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiObjectMapperSupplier defaultOpenApiObjectMapperSupplier() {
        // use swagger-core's object mapper by default
        return Json::mapper;
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaResolver defaultSchemaResolver(OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
                                                SchemaAnnotationMapper schemaAnnotationMapper) {
        return new DefaultSchemaResolver(openApiObjectMapperSupplier, schemaAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameFactory defaultReferenceNameFactory() {
        return new DefaultReferenceNameFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    ReferenceNameConflictResolver defaultReferenceNameConflictResolver() {
        return new DefaultReferenceNameConflictResolver();
    }
}
