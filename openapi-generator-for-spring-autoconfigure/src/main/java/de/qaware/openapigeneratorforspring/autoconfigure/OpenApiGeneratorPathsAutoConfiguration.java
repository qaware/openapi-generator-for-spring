package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationPreFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.PathItemFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.paths.DefaultPathItemSharedItemsCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.PathItemBuilderFactory;
import de.qaware.openapigeneratorforspring.common.paths.PathItemCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenApiGeneratorPathsAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PathsBuilder pathsBuilder(
            HandlerMethodsProvider handlerMethodsProvider,
            PathItemBuilderFactory pathItemBuilderFactory,
            List<PathItemFilter> pathItemFilters,
            List<HandlerMethodFilter> handlerMethodFilters,
            OperationIdConflictResolver operationIdConflictResolver
    ) {
        return new PathsBuilder(handlerMethodsProvider, pathItemBuilderFactory, pathItemFilters, handlerMethodFilters, operationIdConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public PathItemBuilderFactory pathItemBuilderFactory(
            OperationBuilder operationBuilder,
            List<OperationPreFilter> operationPreFilters,
            List<OperationPostFilter> operationPostFilters,
            List<PathItemCustomizer> pathItemCustomizers
    ) {
        return new PathItemBuilderFactory(operationBuilder, operationPreFilters, operationPostFilters, pathItemCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultPathItemSharedItemsCustomizer defaultPathItemSharedItemsCustomizer() {
        return new DefaultPathItemSharedItemsCustomizer();
    }
}
