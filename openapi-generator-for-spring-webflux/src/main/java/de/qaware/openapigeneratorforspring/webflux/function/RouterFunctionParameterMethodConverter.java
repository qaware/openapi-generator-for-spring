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

import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import javax.annotation.Nullable;

public class RouterFunctionParameterMethodConverter implements ParameterMethodConverter {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public Parameter convert(HandlerMethod.Parameter handlerMethodParameter) {
        if (handlerMethodParameter instanceof RouterFunctionHandlerMethod.Parameter) {
            RouterFunctionHandlerMethod.Parameter parameter = (RouterFunctionHandlerMethod.Parameter) handlerMethodParameter;
            return Parameter.builder()
                    .name(parameter.getName()
                            .orElseThrow(() -> new IllegalStateException("Router function parameter should always have a name")))
                    .in(parameter.getParameterIn().toString())
                    .build();
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
