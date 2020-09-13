package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
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
import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferenceDeciderForParameter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferenceNameConflictResolverForParameter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferencedParametersHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorAnnotationAutoConfiguration.class,
})
public class OpenApiGeneratorOperationParameterAutoConfiguration {

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
    public DefaultOperationParameterSchemaCustomizer defaultOperationParameterSchemaCustomizer(
            SchemaResolver schemaResolver,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOperationParameterSchemaCustomizer(schemaResolver, annotationsSupplierFactory);
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

    @Bean
    @ConditionalOnMissingBean
    public ReferencedParametersHandlerFactory referencedParametersHandlerFactory(
            ReferenceNameFactory referenceNameFactory,
            ReferenceNameConflictResolverForParameter referenceNameConflictResolver,
            ReferenceDeciderForParameter referenceDecider
    ) {
        return new ReferencedParametersHandlerFactory(referenceNameFactory, referenceNameConflictResolver, referenceDecider);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceNameConflictResolverForParameter defaultReferenceNameConflictResolverForParameter() {
        return new ReferenceNameConflictResolverForParameter() {
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForParameter defaultReferenceDeciderForParameter() {
        return new ReferenceDeciderForParameter() {
        };
    }
}
