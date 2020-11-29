/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Autoconfigure
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationPreFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.PathItemFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.paths.DefaultPathItemSharedItemsCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
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
            List<HandlerMethod.Merger> handlerMethodMergers,
            PathItemBuilderFactory pathItemBuilderFactory,
            List<PathItemFilter> pathItemFilters,
            List<HandlerMethodFilter> handlerMethodFilters,
            OperationIdConflictResolver operationIdConflictResolver
    ) {
        return new PathsBuilder(handlerMethodsProvider, handlerMethodMergers, pathItemBuilderFactory, pathItemFilters, handlerMethodFilters, operationIdConflictResolver);
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
