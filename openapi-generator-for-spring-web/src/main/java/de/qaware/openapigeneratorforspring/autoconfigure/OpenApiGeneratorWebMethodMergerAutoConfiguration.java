/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Web
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

import de.qaware.openapigeneratorforspring.common.operation.mimetype.ConsumesMimeTypeProviderStrategy;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.ProducesMimeTypeProviderStrategy;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterProvider;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodReturnTypeMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.merger.MergedSpringWebHandlerMethodMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.merger.SpringWebHandlerMethodIdentifierMerger;
import de.qaware.openapigeneratorforspring.common.paths.method.merger.SpringWebHandlerMethodMerger;
import de.qaware.openapigeneratorforspring.common.paths.method.merger.SpringWebHandlerMethodParameterMerger;
import de.qaware.openapigeneratorforspring.common.paths.method.merger.SpringWebHandlerMethodRequestBodyMerger;
import de.qaware.openapigeneratorforspring.common.paths.method.merger.SpringWebHandlerMethodResponseMerger;
import de.qaware.openapigeneratorforspring.common.paths.method.merger.SpringWebHandlerMethodTypeMerger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorWebMethodMergerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMerger defaultSpringWebHandlerMethodMerger(
            SpringWebHandlerMethodParameterMerger springWebHandlerMethodParameterMerger,
            SpringWebHandlerMethodIdentifierMerger springWebHandlerMethodIdentifierMerger
    ) {
        return new SpringWebHandlerMethodMerger(springWebHandlerMethodParameterMerger, springWebHandlerMethodIdentifierMerger);
    }

    @Bean
    @ConditionalOnMissingBean
    public MergedSpringWebHandlerMethodMapper.RequestBodyMapper mergedSpringWebHandSpringWebRequestBodyParameterMapper(
            SpringWebHandlerMethodRequestBodyMerger springWebHandlerMethodRequestBodyMerger
    ) {
        return new MergedSpringWebHandlerMethodMapper.RequestBodyMapper(springWebHandlerMethodRequestBodyMerger);
    }

    @Bean
    @ConditionalOnMissingBean
    public MergedSpringWebHandlerMethodMapper.ResponseMapper mergedSpringWebHandlerMethodResponseMapper(
            SpringWebHandlerMethodResponseMerger springWebHandlerMethodResponseMerger
    ) {
        return new MergedSpringWebHandlerMethodMapper.ResponseMapper(springWebHandlerMethodResponseMerger);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodIdentifierMerger springWebHandlerMethodIdentifierMerger() {
        return new SpringWebHandlerMethodIdentifierMerger();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodTypeMerger springWebHandlerMethodTypeMerger() {
        return new SpringWebHandlerMethodTypeMerger();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodParameterMerger springWebHandlerMethodParameterMerger(SpringWebHandlerMethodTypeMerger springWebHandlerMethodTypeMerger) {
        return new SpringWebHandlerMethodParameterMerger(springWebHandlerMethodTypeMerger);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodRequestBodyMerger springWebHandlerMethodRequestBodyMerger(
            SpringWebHandlerMethodTypeMerger springWebHandlerMethodTypeMerger,
            ConsumesMimeTypeProviderStrategy consumesMimeTypeProviderStrategy,
            SpringWebHandlerMethodRequestBodyParameterProvider springWebHandlerMethodRequestBodyParameterProvider

    ) {
        return new SpringWebHandlerMethodRequestBodyMerger(springWebHandlerMethodTypeMerger, consumesMimeTypeProviderStrategy,
                springWebHandlerMethodRequestBodyParameterProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodResponseMerger springWebHandlerMethodResponseMerger(
            SpringWebHandlerMethodTypeMerger springWebHandlerMethodTypeMerger,
            ProducesMimeTypeProviderStrategy producesMimeTypeProviderStrategy,
            SpringWebHandlerMethodResponseCodeMapper springWebHandlerMethodResponseCodeMapper,
            SpringWebHandlerMethodReturnTypeMapper springWebHandlerMethodReturnTypeMapper
    ) {
        return new SpringWebHandlerMethodResponseMerger(springWebHandlerMethodTypeMerger, producesMimeTypeProviderStrategy,
                springWebHandlerMethodResponseCodeMapper, springWebHandlerMethodReturnTypeMapper);
    }
}
