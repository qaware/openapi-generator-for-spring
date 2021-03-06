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

import de.qaware.openapigeneratorforspring.common.mapper.ApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.DefaultApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseDefaultProvider;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseDefaultProvider;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponsesDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationApiResponsesFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.DefaultOperationResponseCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenApiGeneratorOperationResponseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultOperationResponseCustomizer defaultOperationResponseCustomizer(
            ApiResponseAnnotationMapper apiResponseAnnotationMapper,
            List<OperationApiResponsesCustomizer> apiResponsesCustomizers
    ) {
        return new DefaultOperationResponseCustomizer(apiResponseAnnotationMapper, apiResponsesCustomizers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiResponseAnnotationMapper defaultApiResponseAnnotationMapper(
            HeaderAnnotationMapper headerAnnotationMapper,
            ContentAnnotationMapper contentAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper,
            LinkAnnotationMapper linkAnnotationMapper
    ) {
        return new DefaultApiResponseAnnotationMapper(headerAnnotationMapper,
                contentAnnotationMapper, extensionAnnotationMapper, linkAnnotationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationApiResponsesFromMethodCustomizer defaultOperationApiResponsesFromMethodCustomizer(
            ApiResponseDefaultProvider apiResponseDefaultProvider,
            SchemaResolver schemaResolver,
            List<HandlerMethod.ResponseMapper> handlerMethodResponseMappers
    ) {
        return new DefaultOperationApiResponsesFromMethodCustomizer(apiResponseDefaultProvider, schemaResolver, handlerMethodResponseMappers);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationApiResponsesDescriptionCustomizer operationApiResponsesDescriptionCustomizer() {
        return new DefaultOperationApiResponsesDescriptionCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiResponseDefaultProvider defaultApiResponseDefaultProvider() {
        return new DefaultApiResponseDefaultProvider();
    }
}
