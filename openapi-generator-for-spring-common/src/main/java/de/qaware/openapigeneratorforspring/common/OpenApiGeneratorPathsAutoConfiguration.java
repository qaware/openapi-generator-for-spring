package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.filter.operation.OperationFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.PathItemFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.paths.DefaultPathsBuilder;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
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
}
