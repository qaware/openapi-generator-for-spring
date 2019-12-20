package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.filter.operation.ExcludeHiddenOperationFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.NoOperationsPathItemFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.PathItemFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultDeprecatedOperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationAnnotationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.DefaultOperationIdCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.DefaultOperationIdProvider;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
public class OpenApiGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenApiResource openApiResource(OpenApiGenerator openApiGenerator) {
        return new OpenApiResource(openApiGenerator);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiGenerator openApiGenerator(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            OperationBuilder operationBuilder,
            List<PathItemFilter> pathItemFilters,
            List<OperationFilter> operationFilters,
            OperationIdConflictResolver operationIdConflictResolver
    ) {
        return new OpenApiGenerator(
                requestMappingHandlerMapping,
                operationBuilder,
                pathItemFilters,
                operationFilters,
                operationIdConflictResolver
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
    public DefaultOperationAnnotationCustomizer defaultOperationAnnotationCustomizer() {
        return new DefaultOperationAnnotationCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationIdCustomizer defaultOperationIdCustomizer(OperationIdProvider operationIdProvider) {
        return new DefaultOperationIdCustomizer(operationIdProvider);
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

}
