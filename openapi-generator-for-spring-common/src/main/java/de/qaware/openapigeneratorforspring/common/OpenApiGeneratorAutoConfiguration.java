package de.qaware.openapigeneratorforspring.common;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.annotation.DefaultAnnotationsSupplierFactory;
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
import de.qaware.openapigeneratorforspring.common.operation.parameter.DefaultOperationParameterCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.MethodResponseApiResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.DefaultPathsBuilder;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProviderFromWebMvc;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForNullable;
import de.qaware.openapigeneratorforspring.common.schema.mapper.DefaultSchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolverForCollections;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolverForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolverForSchemaAnnotation;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.SimpleTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.SimpleTypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.SimpleTypeResolverForPrimitiveTypes;
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
            PathsBuilder pathsBuilder,

            ReferenceNameFactory referenceNameFactory,
            ReferenceNameConflictResolver referenceNameConflictResolver
    ) {
        return new OpenApiGenerator(
                pathsBuilder,
                referenceNameFactory,
                referenceNameConflictResolver
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public PathsBuilder defaultPathsBuilder(
            // TODO maybe support list of handlerMethodsProvider?
            HandlerMethodsProvider handlerMethodsProvider,
            OperationBuilder operationBuilder,
            List<PathItemFilter> pathItemFilters,
            List<OperationFilter> operationFilters,
            OperationIdConflictResolver operationIdConflictResolver
    ) {
        return new DefaultPathsBuilder(handlerMethodsProvider, operationBuilder, pathItemFilters, operationFilters, operationIdConflictResolver);
    }


    // TODO extract this to maven module with Spring Web MVC dependency?
    @Bean
    public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new HandlerMethodsProviderFromWebMvc(requestMappingHandlerMapping);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationBuilder operationBuilder(List<OperationCustomizer> operationCustomizers, AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new OperationBuilder(operationCustomizers, annotationsSupplierFactory);
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
    public DefaultDeprecatedOperationCustomizer defaultDeprecatedOperationCustomizer(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new DefaultDeprecatedOperationCustomizer(annotationsSupplierFactory);
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
            List<OperationApiResponseCustomizer> apiResponsesCustomizers,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOperationResponseCustomizer(
                headerAnnotationMapper, contentAnnotationMapper,
                extensionAnnotationMapper, linkAnnotationMapper,
                apiResponseCodeMapper, apiResponsesCustomizers,
                annotationsSupplierFactory
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodResponseApiResponseCustomizer methodResponseApiResponseCustomizer(
            DefaultApiResponseCodeMapper defaultApiResponseCodeMapper, SchemaResolver schemaResolver,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new MethodResponseApiResponseCustomizer(defaultApiResponseCodeMapper, schemaResolver, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultApiResponseCodeMapper defaultApiResponseCodeMapper(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new DefaultApiResponseCodeMapper(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationParameterCustomizer defaultOperationParameterCustomizer(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new DefaultOperationParameterCustomizer(annotationsSupplierFactory);
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
    public ExcludeHiddenOperationFilter excludeHiddenOperationFilter(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new ExcludeHiddenOperationFilter(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiObjectMapperSupplier defaultOpenApiObjectMapperSupplier() {
        // use swagger-core's object mapper by default
        // TODO consider this choice again: Maybe the "auto-configured" object
        //  mapper from spring would work better?
        return () -> Json.mapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaResolver defaultSchemaResolver(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            SchemaAnnotationMapperFactory schemaAnnotationMapperFactory,
            AnnotationsSupplierFactory annotationsSupplierFactory,
            List<GenericTypeResolver> genericTypeResolvers,
            List<SimpleTypeResolver> simpleTypeResolvers,
            List<SchemaCustomizer> schemaCustomizers
    ) {
        return new DefaultSchemaResolver(
                openApiObjectMapperSupplier, schemaAnnotationMapperFactory, annotationsSupplierFactory,
                genericTypeResolvers, simpleTypeResolvers, schemaCustomizers
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public AnnotationsSupplierFactory defaultAnnotationsSupplierFactory() {
        return new DefaultAnnotationsSupplierFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public GenericTypeResolverForCollections defaultGenericTypeResolverForCollections() {
        return new GenericTypeResolverForCollections();
    }


    @Bean
    @ConditionalOnMissingBean
    public GenericTypeResolverForReferenceType defaultGenericTypeResolverForReferenceType() {
        return new GenericTypeResolverForReferenceType();
    }


    @Bean
    @ConditionalOnMissingBean
    public GenericTypeResolverForObject defaultGenericTypeResolverForObject() {
        return new GenericTypeResolverForObject();
    }

    @Bean
    @ConditionalOnMissingBean
    public GenericTypeResolverForSchemaAnnotation defaultGenericTypeResolverForSchemaAnnotation(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new GenericTypeResolverForSchemaAnnotation(openApiObjectMapperSupplier, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleTypeResolverForPrimitiveTypes defaultSimpleTypeResolverForPrimitiveTypes() {
        return new SimpleTypeResolverForPrimitiveTypes();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleTypeResolverForObject defaultSimpleTypeResolverForObject(SchemaNameFactory schemaNameFactory) {
        return new SimpleTypeResolverForObject(schemaNameFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaCustomizerForNullable defaultSchemaCustomizerForNullable() {
        return new SchemaCustomizerForNullable();
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

    @Bean
    @ConditionalOnMissingBean
    public SchemaNameFactory defaultSchemaNameFactory() {
        return new DefaultSchemaNameFactory();
    }

}
