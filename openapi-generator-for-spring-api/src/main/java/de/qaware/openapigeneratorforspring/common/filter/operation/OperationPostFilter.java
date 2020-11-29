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

package de.qaware.openapigeneratorforspring.common.filter.operation;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.operation.Operation;

/**
 * Filter for {@link Operation Operations}. Is evaluated AFTER the operation is constructed.
 *
 * <p> This filter does not remove possibly referenced items due to operation
 * building. Prefer using {@link OperationPreFilter} whenever possible.
 *
 * @see OperationPreFilter
 */
@FunctionalInterface
public interface OperationPostFilter {
    /**
     * Accept the given operation.
     *
     * @param operation     built operation
     * @param handlerMethod handler method used to build this operation
     * @return true if it shall be included, false otherwise
     */
    boolean postAccept(Operation operation, HandlerMethod handlerMethod);
}
