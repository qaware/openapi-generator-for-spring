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

package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

import java.util.Optional;

/**
 * Context for {@link OperationParameterCustomizer}.
 *
 * <p>Extends {@link OperationBuilderContext} with an optional
 * {@link HandlerMethod.Parameter handler method paramter}.
 */
public interface OperationParameterCustomizerContext extends OperationBuilderContext {
    /**
     * Optional handler method parameter from which the to
     * be customized Parameter was converted from.
     *
     * @return Optional handler method parameter
     */
    Optional<HandlerMethod.Parameter> getHandlerMethodParameter();
}
