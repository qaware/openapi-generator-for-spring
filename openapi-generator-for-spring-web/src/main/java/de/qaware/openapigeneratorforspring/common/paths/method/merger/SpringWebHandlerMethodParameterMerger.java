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
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod.SpringWebParameter;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class SpringWebHandlerMethodParameterMerger {

    private final SpringWebHandlerMethodTypeMerger springWebHandlerMethodTypeMerger;

    public List<SpringWebParameter> mergeParameters(List<SpringWebHandlerMethod> handlerMethods) {
        return handlerMethods.stream()
                .flatMap(handlerMethod -> handlerMethod.getSpringWebParameters().stream()
                        .map(parameter -> buildExtendedParameterPair(handlerMethod, parameter))
                )
                .collect(OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList())
                .entrySet().stream()
                .map(entry -> entry.getKey()
                        .map(parameterName -> {
                            val parameters = entry.getValue().stream()
                                    .map(ExtendedParameter::getParameter)
                                    .collect(Collectors.toList());
                            val handlerMethodsForParameters = entry.getValue().stream()
                                    .map(ExtendedParameter::getOwningHandlerMethod)
                                    .collect(Collectors.toCollection(LinkedHashSet::new));
                            return buildMergedParameter(parameterName, parameters, parameter -> {
                                if (!handlerMethodsForParameters.containsAll(handlerMethods)) {
                                    parameter.setRequired(false); // otherwise, the Swagger UI doesn't work
                                    if (parameter.getDescription() == null) {
                                        parameter.setDescription("Only used by " + handlerMethodsForParameters.stream()
                                                .map(HandlerMethod::getIdentifier)
                                                .collect(Collectors.joining(", "))
                                        );
                                    }
                                }
                            });
                        })
                        .orElseThrow(() -> new IllegalStateException("Cannot merge handler methods with unnamed parameters: " + entry.getValue()))
                )
                .collect(Collectors.toList());
    }

    private Pair<Optional<String>, ExtendedParameter> buildExtendedParameterPair(SpringWebHandlerMethod handlerMethod, SpringWebParameter parameter) {
        return Pair.of(parameter.getName(), ExtendedParameter.of(parameter, handlerMethod));
    }

    private SpringWebParameter buildMergedParameter(String parameterName, List<SpringWebParameter> parameters, Consumer<Parameter> parameterCustomizer) {
        val mergedAnnotationsSupplier = AnnotationsSupplier.merge(parameters.stream());
        val mergedType = springWebHandlerMethodTypeMerger.mergeTypes(parameters.stream().map(SpringWebParameter::getParameterType))
                .orElseThrow(() -> new IllegalStateException("Grouped parameters should contain at least one entry"));
        return new SpringWebParameter(parameterName, mergedType, mergedAnnotationsSupplier) {
            @Override
            public void customize(Parameter parameter) {
                parameterCustomizer.accept(parameter);
            }
        };
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class ExtendedParameter {
        private final SpringWebParameter parameter;
        private final HandlerMethod owningHandlerMethod;
    }
}
