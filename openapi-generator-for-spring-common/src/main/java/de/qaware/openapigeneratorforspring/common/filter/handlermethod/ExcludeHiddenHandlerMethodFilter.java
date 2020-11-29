/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
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

package de.qaware.openapigeneratorforspring.common.filter.handlermethod;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

public class ExcludeHiddenHandlerMethodFilter implements HandlerMethodFilter {
    @Override
    public boolean accept(HandlerMethodWithInfo handlerMethodWithInfo) {
        HandlerMethod handlerMethod = handlerMethodWithInfo.getHandlerMethod();
        boolean hiddenOnHandlerMethod = handlerMethod
                .findAnnotations(Hidden.class)
                .findAny().isPresent();
        return !hiddenOnHandlerMethod && handlerMethod.findAnnotations(Operation.class).noneMatch(Operation::hidden);
    }
}
