package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPreFilter;
import de.qaware.openapigeneratorforspring.common.mapper.*;
import de.qaware.openapigeneratorforspring.common.operation.parameter.DefaultOperationParameterCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.*;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
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
            List<HandlerMethod.ParameterTypeMapper> handlerMethodParameterTypeMappers
    ) {
        return new DefaultOperationParameterSchemaCustomizer(schemaResolver, handlerMethodParameterTypeMappers);
    }
}
