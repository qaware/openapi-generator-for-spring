package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.CallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultDeprecatedOperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationCallbackCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationIdCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationTagsCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultRequestBodyOperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdProvider;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorAnnotationAutoConfiguration.class,
        OpenApiGeneratorMapperAutoConfiguration.class,
        OpenApiGeneratorOperationResponseAutoConfiguration.class,
        OpenApiGeneratorOperationParameterAutoConfiguration.class,
})
public class OpenApiGeneratorOperationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OperationBuilder operationBuilder(OperationAnnotationMapper operationAnnotationMapper,
                                             List<OperationCustomizer> operationCustomizers,
                                             AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new OperationBuilder(operationAnnotationMapper, operationCustomizers, annotationsSupplierFactory);
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
    public DefaultOperationTagsCustomizer defaultOperationTagsCustomizer(
            TagAnnotationMapper tagAnnotationMapper,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOperationTagsCustomizer(tagAnnotationMapper, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationIdCustomizer defaultOperationIdCustomizer(OperationIdProvider operationIdProvider) {
        return new DefaultOperationIdCustomizer(operationIdProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultRequestBodyOperationCustomizer defaultRequestBodyOperationCustomizer(
            RequestBodyAnnotationMapper requestBodyAnnotationMapper,
            AnnotationsSupplierFactory annotationsSupplierFactory,
            SchemaResolver schemaResolver
    ) {
        return new DefaultRequestBodyOperationCustomizer(requestBodyAnnotationMapper, annotationsSupplierFactory, schemaResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationCallbackCustomizer defaultOperationCallbackCustomizer(
            CallbackAnnotationMapper callbackAnnotationMapper,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultOperationCallbackCustomizer(callbackAnnotationMapper, annotationsSupplierFactory);
    }
}
