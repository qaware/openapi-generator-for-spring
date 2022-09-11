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

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.ConsumesMimeTypeProviderStrategy;
import de.qaware.openapigeneratorforspring.common.operation.mimetype.ProducesMimeTypeProviderStrategy;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterProvider;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodReturnTypeMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(OpenApiGeneratorWebMethodMergerAutoConfiguration.class)
public class OpenApiGeneratorWebMethodAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.ContextModifierMapper springWebHandlerMethodContextAwareMapper(
            ConsumesMimeTypeProviderStrategy consumesMimeTypeProviderStrategy,
            ProducesMimeTypeProviderStrategy producesMimeTypeProviderStrategy
    ) {
        return new SpringWebHandlerMethodMapper.ContextModifierMapper(consumesMimeTypeProviderStrategy, producesMimeTypeProviderStrategy);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.RequestBodyMapper springWebHandlerMethodRequestBodyMapper(
            ConsumesMimeTypeProviderStrategy consumesMimeTypeProviderStrategy,
            SpringWebHandlerMethodRequestBodyParameterProvider springWebHandlerMethodRequestBodyParameterProvider
    ) {
        return new SpringWebHandlerMethodMapper.RequestBodyMapper(consumesMimeTypeProviderStrategy, springWebHandlerMethodRequestBodyParameterProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodMapper.ResponseMapper springWebHandlerMethodResponseMapper(
            ProducesMimeTypeProviderStrategy producesMimeTypeProviderStrategy,
            SpringWebHandlerMethodResponseCodeMapper springWebHandlerMethodResponseCodeMapper,
            SpringWebHandlerMethodReturnTypeMapper springWebHandlerMethodReturnTypeMapper
    ) {
        return new SpringWebHandlerMethodMapper.ResponseMapper(producesMimeTypeProviderStrategy, springWebHandlerMethodResponseCodeMapper, springWebHandlerMethodReturnTypeMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodRequestBodyParameterProvider springWebHandlerMethodRequestBodyParameterProvider() {
        return new SpringWebHandlerMethodRequestBodyParameterProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodReturnTypeMapper springWebHandlerMethodReturnTypeMapper(AnnotationsSupplierFactory annotationsSupplierFactory) {
        return new SpringWebHandlerMethodReturnTypeMapper(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringWebHandlerMethodResponseCodeMapper springWebHandlerMethodResponseCodeMapper() {
        return new SpringWebHandlerMethodResponseCodeMapper();
    }
}
