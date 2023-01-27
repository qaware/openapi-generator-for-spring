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

package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class SpringWebHandlerMethodMerger implements HandlerMethod.Merger {

    private final SpringWebHandlerMethodParameterMerger springWebHandlerMethodParameterMerger;
    private final SpringWebHandlerMethodIdentifierMerger springWebHandlerMethodIdentifierMerger;

    @Nullable
    @Override
    public HandlerMethod merge(List<HandlerMethod> handlerMethods) {
        return findSpringWebHandlerMethods(handlerMethods)
                .map(springWebHandlerMethods -> new MergedSpringWebHandlerMethod(
                        springWebHandlerMethodParameterMerger.mergeParameters(springWebHandlerMethods),
                        springWebHandlerMethodIdentifierMerger.mergeIdentifiers(springWebHandlerMethods),
                        // until here, merging is independent of springWebHandlerMethods
                        // but eventually providing merged request bodies and merged responses
                        // requires assumptions about how Spring Web handler methods work
                        springWebHandlerMethods
                ))
                .orElse(null);
    }

    private Optional<List<SpringWebHandlerMethod>> findSpringWebHandlerMethods(List<HandlerMethod> handlerMethods) {
        List<SpringWebHandlerMethod> springWebHandlerMethods = handlerMethods.stream()
                .map(handlerMethod -> handlerMethod instanceof SpringWebHandlerMethod springWebHandlerMethod ? springWebHandlerMethod : null)
                .filter(Objects::nonNull)
                // we need a stable ordering of the discovered handler methods
                // the order how Spring provides the handler methods appears to be undefined,
                // and at least for merging, we need it to be ordered
                .sorted(Comparator.comparing(SpringWebHandlerMethodMerger::getComparingKey))
                .toList();
        if (springWebHandlerMethods.isEmpty()) {
            return Optional.empty();
        }
        if (springWebHandlerMethods.size() != handlerMethods.size()) {
            throw new IllegalStateException("Cannot map handler methods not purely of type " + SpringWebHandlerMethod.class.getSimpleName());
        }
        return Optional.of(springWebHandlerMethods);
    }

    private static String getComparingKey(SpringWebHandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod().getMethod();
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }
}
