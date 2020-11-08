package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.*;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.customizer.*;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdProvider;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorOperationParameterAutoConfiguration.class,
        OpenApiGeneratorOperationResponseAutoConfiguration.class
})
public class OpenApiGeneratorOperationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OperationBuilder operationBuilder(OperationAnnotationMapper operationAnnotationMapper, List<OperationCustomizer> operationCustomizers) {
        return new OperationBuilder(operationAnnotationMapper, operationCustomizers);
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
    public DefaultOperationTagsCustomizer defaultOperationTagsCustomizer(TagAnnotationMapper tagAnnotationMapper) {
        return new DefaultOperationTagsCustomizer(tagAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationSecuritySchemesCustomizer defaultOperationSecuritySchemesCustomizer(SecuritySchemeAnnotationMapper securitySchemeAnnotationMapper) {
        return new DefaultOperationSecuritySchemesCustomizer(securitySchemeAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationServersCustomizer defaultOperationServersCustomizer(ServerAnnotationMapper serverAnnotationMapper) {
        return new DefaultOperationServersCustomizer(serverAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationSecurityRequirementCustomizer defaultOperationSecurityRequirementCustomizer(SecurityRequirementAnnotationMapper securityRequirementAnnotationMapper) {
        return new DefaultOperationSecurityRequirementCustomizer(securityRequirementAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationIdCustomizer defaultOperationIdCustomizer(OperationIdProvider operationIdProvider) {
        return new DefaultOperationIdCustomizer(operationIdProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationCallbackCustomizer defaultOperationCallbackCustomizer(CallbackAnnotationMapper callbackAnnotationMapper) {
        return new DefaultOperationCallbackCustomizer(callbackAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultRequestBodyOperationCustomizer defaultRequestBodyOperationCustomizer(
            RequestBodyAnnotationMapper requestBodyAnnotationMapper,
            List<HandlerMethod.RequestBodyParameterMapper> handlerMethodRequestBodyParameterMappers,
            SchemaResolver schemaResolver
    ) {
        return new DefaultRequestBodyOperationCustomizer(requestBodyAnnotationMapper, handlerMethodRequestBodyParameterMappers, schemaResolver);
    }
}
