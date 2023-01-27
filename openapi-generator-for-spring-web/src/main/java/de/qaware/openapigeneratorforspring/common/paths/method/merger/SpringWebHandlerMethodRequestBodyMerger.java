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

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod.SpringWebRequestBody;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodRequestBodyParameterMapper.RequestBodyParameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodContentTypesMapper.SINGLE_ALL_VALUE;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class SpringWebHandlerMethodRequestBodyMerger {

    private final SpringWebHandlerMethodTypeMerger typeMerger;
    private final SpringWebHandlerMethodContentTypesMapper contentTypesMapper;
    private final SpringWebHandlerMethodRequestBodyParameterMapper requestBodyParameterMapper;

    public List<HandlerMethod.RequestBody> mergeRequestBodies(List<SpringWebHandlerMethod> handlerMethods) {
        // group request bodies by (unique) list of consumesContentTypes
        // this way, we can detect best how to build the final request body list
        Map<Set<String>, List<Optional<RequestBodyParameter>>> groupedRequestBodyParameters = handlerMethods.stream()
                .map(handlerMethod -> Pair.of(contentTypesMapper.findConsumesContentTypes(handlerMethod), requestBodyParameterMapper.findRequestBodyParameter(handlerMethod)))
                .collect(groupingByPairKeyAndCollectingValuesToList());

        List<Optional<RequestBodyParameter>> allValueRequestBodyParameters = groupedRequestBodyParameters.get(SINGLE_ALL_VALUE);
        if (groupedRequestBodyParameters.size() == 1
                && allValueRequestBodyParameters != null && allValueRequestBodyParameters.stream().noneMatch(Optional::isPresent)) {
            // we can omit the request bodies entirely if there's only empty request bodies matching for ALL_VALUE
            return emptyList();
        }

        boolean noEmptyEntries = groupedRequestBodyParameters.values().stream()
                .flatMap(Collection::stream)
                .allMatch(Optional::isPresent);

        return groupedRequestBodyParameters.entrySet().stream()
                .map(entry -> {
                    Set<String> consumesContentTypes = entry.getKey();
                    List<RequestBodyParameter> requestBodyParameters = entry.getValue().stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList();
                    return typeMerger.mergeTypes(requestBodyParameters.stream().map(RequestBodyParameter::getParameter))
                            .map(parameterType -> {
                                boolean required = requestBodyParameters.stream()
                                        .map(RequestBodyParameter::isRequired)
                                        .reduce((a, b) -> a || b)
                                        .orElse(false);
                                return new SpringWebRequestBody(
                                        AnnotationsSupplier.merge(requestBodyParameters.stream().map(RequestBodyParameter::getParameter)),
                                        consumesContentTypes,
                                        Optional.of(parameterType),
                                        noEmptyEntries ? required : null
                                ) {
                                    @Nullable
                                    @Override
                                    public HandlerMethod.Context getContext() {
                                        return MergedSpringWebHandlerMethodContext.of(consumesContentTypes);
                                    }
                                };
                            })
                            .map(HandlerMethod.RequestBody.class::cast)
                            // do not omit request body entry just because the handler method doesn't have a parameter here
                            .orElseGet(() -> new EmptyRequestBody(consumesContentTypes));
                })
                .toList();
    }

    @RequiredArgsConstructor
    private static class EmptyRequestBody implements HandlerMethod.RequestBody {
        @Getter
        private final Set<String> consumesContentTypes;

        @Override
        public Optional<HandlerMethod.Type> getType() {
            return Optional.empty();
        }

        @Override
        public AnnotationsSupplier getAnnotationsSupplier() {
            return AnnotationsSupplier.EMPTY;
        }
    }

}
