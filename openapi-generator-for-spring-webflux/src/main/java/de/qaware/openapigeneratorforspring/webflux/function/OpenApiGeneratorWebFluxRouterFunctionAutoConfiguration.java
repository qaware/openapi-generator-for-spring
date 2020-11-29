/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebFlux
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

package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorWebFluxRouterFunctionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public RouterFunctionHandlerMethodWithInfoBuilder routerFunctionHandlerMethodWithInfoBuilder(
            AnnotationsSupplierFactory annotationsSupplierFactory,
            ConfigurableListableBeanFactory beanFactory
    ) {
        return new RouterFunctionHandlerMethodWithInfoBuilder(annotationsSupplierFactory, beanFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterFunctionHandlerMethodMapper.RequestBodyMapper routerFunctionRequestBodyParameterMapper() {
        return new RouterFunctionHandlerMethodMapper.RequestBodyMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterFunctionHandlerMethodMapper.ResponseMapper routerFunctionReturnTypeMapper() {
        return new RouterFunctionHandlerMethodMapper.ResponseMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterFunctionParameterMethodConverter routerFunctionParameterMethodConverter() {
        return new RouterFunctionParameterMethodConverter();
    }
}
