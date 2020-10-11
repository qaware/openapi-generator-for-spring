package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.PathItemFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.paths.DefaultPathItemParametersCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.PathItemBuilderFactory;
import de.qaware.openapigeneratorforspring.common.paths.PathItemCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorOperationAutoConfiguration.class,
        OpenApiGeneratorFilterAutoConfiguration.class
})
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
            List<OperationFilter> operationFilters,
            List<PathItemCustomizer> pathItemCustomizers
    ) {
        return new PathItemBuilderFactory(operationBuilder, operationFilters, pathItemCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultPathItemParametersCustomizer defaultPathItemParametersCustomizer() {
        return new DefaultPathItemParametersCustomizer();
    }
}
