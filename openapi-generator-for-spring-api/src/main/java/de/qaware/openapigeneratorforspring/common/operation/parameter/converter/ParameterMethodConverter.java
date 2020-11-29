/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
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

package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import javax.annotation.Nullable;

/**
 * Converter for {@link  HandlerMethod.Parameter handler method
 * parameter}. Only if such a converter exists, the handler
 * method parameter will become part of the operation parameters.
 */
@FunctionalInterface
public interface ParameterMethodConverter extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Convert the given {@link  HandlerMethod.Parameter handler
     * method parameter} into an {@link Parameter operation parameter}.
     *
     * @param handlerMethodParameter handler method parameter
     * @return parameter, or null if nothing could be converted
     */
    @Nullable
    Parameter convert(HandlerMethod.Parameter handlerMethodParameter);
}
