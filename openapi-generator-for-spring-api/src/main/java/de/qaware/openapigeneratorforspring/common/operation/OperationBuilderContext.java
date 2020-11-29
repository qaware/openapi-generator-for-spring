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

package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.HasReferencedItemConsumer;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Context for operation building. Carries operation info, supports referencing
 * items and provides mapper context for additional annotation analysis.
 */
public interface OperationBuilderContext extends HasReferencedItemConsumer {
    /**
     * Get the operation info, containing for example the handler method. See also {@link #getHandlerMethod}.
     *
     * @return operation info
     */
    OperationInfo getOperationInfo();

    /**
     * Obtain the mapper context, requiring an optional handler method
     * context for {@link HandlerMethod.ContextModifierMapper} application.
     *
     * @param context handler method context (maybe null)
     * @return mapper context to be used for additional annotation mapping
     */
    MapperContext getMapperContext(@Nullable HandlerMethod.Context context);

    /**
     * Convenience method to obtain a handler method of certain type if possible.
     *
     * @param handlerMethodType type of handler method implementation
     * @param <M>               type of handler method
     * @return handler method, or empty if not of requested type
     */
    default <M extends HandlerMethod> Optional<M> getHandlerMethod(Class<M> handlerMethodType) {
        return Optional.of(getOperationInfo().getHandlerMethod())
                .filter(handlerMethodType::isInstance)
                .map(handlerMethodType::cast);
    }
}
