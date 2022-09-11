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

import de.qaware.openapigeneratorforspring.common.operation.mimetype.ProducesMimeTypeProviderStrategy;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodResponseCodeMapper;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethodReturnTypeMapper;
import de.qaware.openapigeneratorforspring.model.media.Content;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.MimeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesTo;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;

@RequiredArgsConstructor
public class SpringWebHandlerMethodResponseMerger {

    private final SpringWebHandlerMethodTypeMerger typeMerger;
    private final ProducesMimeTypeProviderStrategy producesMimeTypeProviderStrategy;
    private final SpringWebHandlerMethodResponseCodeMapper responseCodeMapper;
    private final SpringWebHandlerMethodReturnTypeMapper returnTypeMapper;

    public List<HandlerMethod.Response> mergeResponses(List<SpringWebHandlerMethod> handlerMethods) {
        List<HandlerMethod.Response> mergedResponses = new ArrayList<>();
        Map<String, Map<Set<MimeType>, List<SpringWebHandlerMethod>>> groupedHandlerMethods = handlerMethods.stream()
                .map(method -> Pair.of(responseCodeMapper.getResponseCode(method), Pair.of(producesMimeTypeProviderStrategy.getProducesMimeTypes(method), method)))
                .collect(groupingByPairKeyAndCollectingValuesTo(groupingByPairKeyAndCollectingValuesToList()));

        groupedHandlerMethods.forEach((responseCode, typesGroupedByProduces) ->
                typesGroupedByProduces.forEach((producesContentTypes, methods) ->
                        mergedResponses.add(
                                new AbstractSpringWebHandlerMethod.SpringWebResponse(
                                        responseCode,
                                        producesContentTypes,
                                        mergeReturnTypes(methods)
                                ) {
                                    @Override
                                    public boolean shouldClearContent(Content content) {
                                        return groupedHandlerMethods.get(responseCode).size() == 1;
                                    }
                                }
                        )
                )
        );
        return mergedResponses;
    }

    private Optional<HandlerMethod.Type> mergeReturnTypes(List<SpringWebHandlerMethod> handlerMethods) {
        return handlerMethods.stream()
                .map(returnTypeMapper::getReturnType)
                .reduce(typeMerger::mergeType)
                .map(x -> x);
    }
}
