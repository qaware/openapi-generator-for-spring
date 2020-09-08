package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultDeprecatedOperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationExternalDocsCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationIdCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationServersCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationSummaryAndDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationTagsCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdProvider;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import de.qaware.openapigeneratorforspring.common.operation.parameter.DefaultOperationParameterCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.DefaultParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.parameter.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterBuilderFromSpringWebAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromPathVariableAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromRequestHeaderAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.DefaultParameterMethodConverterFromRequestParamAnnotation;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.DefaultOperationParameterAnnotationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.DefaultOperationParameterDeprecatedCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.DefaultOperationParameterMethodNameCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.DefaultOperationParameterNullableCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.DefaultOperationParameterSchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponseFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponseFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorAnnotationAutoConfiguration.class,
        OpenApiGeneratorMapperAutoConfiguration.class
})
public class OpenApiGeneratorOperationAutoConfiguration {

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
    public OperationApiResponseFromMethodCustomizer defaultOperationApiResponseFromMethodCustomizer(
            DefaultApiResponseCodeMapper defaultApiResponseCodeMapper, SchemaResolver schemaResolver,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOperationApiResponseFromMethodCustomizer(defaultApiResponseCodeMapper, schemaResolver, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultApiResponseCodeMapper defaultApiResponseCodeMapper(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new DefaultApiResponseCodeMapper(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationParameterCustomizer defaultOperationParameterCustomizer(
            AnnotationsSupplierFactory annotationsSupplierFactory,
            List<ParameterMethodConverter> parameterMethodConverters,
            List<OperationParameterCustomizer> operationParameterCustomizers,
            ParameterAnnotationMapper parameterAnnotationMapper
    ) {
        return new DefaultOperationParameterCustomizer(
                annotationsSupplierFactory, parameterMethodConverters, operationParameterCustomizers, parameterAnnotationMapper
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterAnnotationMapper defaultParameterAnnotationMapper(
            SchemaAnnotationMapper schemaAnnotationMapper,
            ContentAnnotationMapper contentAnnotationMapper,
            ExampleObjectAnnotationMapper exampleObjectAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new DefaultParameterAnnotationMapper(
                schemaAnnotationMapper, contentAnnotationMapper, exampleObjectAnnotationMapper, extensionAnnotationMapper
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationParameterAnnotationCustomizer defaultOperationParameterAnnotationCustomizer(ParameterAnnotationMapper parameterAnnotationMapper) {
        return new DefaultOperationParameterAnnotationCustomizer(parameterAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationParameterDeprecatedCustomizer defaultOperationParameterDeprecatedCustomizer() {
        return new DefaultOperationParameterDeprecatedCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationParameterMethodNameCustomizer defaultOperationParameterMethodNameCustomizer() {
        return new DefaultOperationParameterMethodNameCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationParameterNullableCustomizer defaultOperationParameterNullableCustomizer() {
        return new DefaultOperationParameterNullableCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationParameterSchemaCustomizer defaultOperationParameterSchemaCustomizer(SchemaResolver schemaResolver) {
        return new DefaultOperationParameterSchemaCustomizer(schemaResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterBuilderFromSpringWebAnnotation defaultParameterBuilderFromSpringWebAnnotation() {
        return new DefaultParameterBuilderFromSpringWebAnnotation();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromPathVariableAnnotation defaultParameterMethodConverterFromPathVariableAnnotation(
            DefaultParameterBuilderFromSpringWebAnnotation defaultParameterBuilderFromSpringWebAnnotation
    ) {
        return new DefaultParameterMethodConverterFromPathVariableAnnotation(defaultParameterBuilderFromSpringWebAnnotation);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromRequestHeaderAnnotation defaultParameterMethodConverterFromRequestHeaderAnnotation(
            DefaultParameterBuilderFromSpringWebAnnotation defaultParameterBuilderFromSpringWebAnnotation
    ) {
        return new DefaultParameterMethodConverterFromRequestHeaderAnnotation(defaultParameterBuilderFromSpringWebAnnotation);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultParameterMethodConverterFromRequestParamAnnotation defaultParameterMethodConverterFromRequestParamAnnotation(
            DefaultParameterBuilderFromSpringWebAnnotation defaultParameterBuilderFromSpringWebAnnotation
    ) {
        return new DefaultParameterMethodConverterFromRequestParamAnnotation(defaultParameterBuilderFromSpringWebAnnotation);
    }
}
