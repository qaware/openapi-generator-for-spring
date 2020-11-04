package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPreFilter;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
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
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodParameterTypeMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenApiGeneratorOperationParameterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationParameterCustomizer defaultOperationParameterCustomizer(
            List<OperationParameterPreFilter> operationParameterPreFilters,
            List<OperationParameterPostFilter> operationParameterPostFilters,
            List<ParameterMethodConverter> parameterMethodConverters,
            List<OperationParameterCustomizer> operationParameterCustomizers,
            ParameterAnnotationMapper parameterAnnotationMapper
    ) {
        return new DefaultOperationParameterCustomizer(
                operationParameterPreFilters, operationParameterPostFilters,
                parameterMethodConverters, operationParameterCustomizers, parameterAnnotationMapper
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
    public DefaultOperationParameterSchemaCustomizer defaultOperationParameterSchemaCustomizer(
            SchemaResolver schemaResolver,
            HandlerMethodParameterTypeMapper handlerMethodParameterTypeMapper
    ) {
        return new DefaultOperationParameterSchemaCustomizer(schemaResolver, handlerMethodParameterTypeMapper);
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
