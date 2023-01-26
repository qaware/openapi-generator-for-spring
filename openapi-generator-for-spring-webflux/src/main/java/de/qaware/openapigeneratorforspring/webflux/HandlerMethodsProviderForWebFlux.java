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

package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodEnumMapper;
import de.qaware.openapigeneratorforspring.webflux.function.RouterFunctionHandlerMethodWithInfoBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class HandlerMethodsProviderForWebFlux implements HandlerMethodsProvider {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder;
    private final SpringWebRequestMethodEnumMapper springWebRequestMethodEnumMapper;

    private final Map<String, RouterFunction<?>> routerFunctions;
    private final RouterFunctionHandlerMethodWithInfoBuilder routerFunctionHandlerMethodWithInfoBuilder;

    @Override
    public List<HandlerMethodWithInfo> getHandlerMethods() {
        return Stream.concat(
                        getHandlerMethodsFromRequestMappings(),
                        getHandlerMethodsFromRouterFunctions()
                )
                .collect(Collectors.toList());
    }

    private Stream<HandlerMethodWithInfo> getHandlerMethodsFromRequestMappings() {
        return requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                .map(entry -> new HandlerMethodWithInfo(
                        springWebHandlerMethodBuilder.build(entry.getValue()),
                        entry.getKey().getPatternsCondition().getPatterns().stream()
                                .map(PathPattern::toString)
                                .collect(Collectors.toCollection(LinkedHashSet::new)),
                        springWebRequestMethodEnumMapper.map(entry.getKey().getMethodsCondition().getMethods())
                ));
    }

    private Stream<HandlerMethodWithInfo> getHandlerMethodsFromRouterFunctions() {
        return routerFunctions.entrySet().stream().map(entry -> routerFunctionHandlerMethodWithInfoBuilder.build(entry.getKey(), entry.getValue()));
    }
}
