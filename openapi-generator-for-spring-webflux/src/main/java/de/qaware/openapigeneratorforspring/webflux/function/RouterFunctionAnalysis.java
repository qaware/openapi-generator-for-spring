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

package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.path.RequestMethod;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
class RouterFunctionAnalysis implements RouterFunctions.Visitor, RequestPredicates.Visitor {

    @Getter
    private final Result result = new Result();

    static RouterFunctionAnalysis.Result analyze(RouterFunction<?> routerFunction) {
        RouterFunctionAnalysis visitor = new RouterFunctionAnalysis();
        routerFunction.accept(visitor);
        return visitor.getResult();
    }

    // RouterFunctions.Visitor

    @Override
    public void startNested(RequestPredicate predicate) {
        // see GH Issue #4
    }

    @Override
    public void endNested(RequestPredicate predicate) {
        // ignored
    }

    @Override
    public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
        predicate.accept(this);
    }

    @Override
    public void resources(Function<ServerRequest, Mono<Resource>> lookupFunction) {
        // ignored
    }

    @Override
    public void attributes(Map<String, Object> attributes) {
        // ignored
    }

    @Override
    public void unknown(RouterFunction<?> routerFunction) {
        // ignored
    }

    // RequestPredicates.Visitor

    @Override
    public void method(Set<HttpMethod> methods) {
        methods.stream()
            .map(HttpMethod::name)
            .map(RequestMethod::valueOf)
            .forEach(result.requestMethods::add);
    }

    @Override
    public void path(String pattern) {
        result.paths.add(pattern);
    }

    @Override
    public void pathExtension(String extension) {
        // ignored
    }

    @Override
    public void header(String name, String value) {
        // client sends those headers and we match against it,
        // so it wants to consume what we produce
        if (HttpHeaders.ACCEPT.equals(name)) {
            // see also org.springframework.web.reactive.function.server.RequestPredicates.AcceptPredicate
            result.producesContentTypesFromHeader.add(value);
        }
        else if (HttpHeaders.CONTENT_TYPE.equals(name)) {
            // see also org.springframework.web.reactive.function.server.RequestPredicates.ContentTypePredicate
            result.consumesContentTypesFromHeader.add(value);
        }
        else {
            result.parameters.add(new RouterFunctionHandlerMethod.Parameter(name, ParameterIn.HEADER));
        }
    }

    @Override
    public void queryParam(String name, String value) {
        result.parameters.add(new RouterFunctionHandlerMethod.Parameter(name, ParameterIn.QUERY));
    }

    @Override
    public void startAnd() {
        // ignored
    }

    @Override
    public void and() {
        // ignored
    }

    @Override
    public void endAnd() {
        // ignored
    }

    @Override
    public void startOr() {
        // ignored
    }

    @Override
    public void or() {
        // ignored

    }

    @Override
    public void endOr() {
        // ignored
    }

    @Override
    public void startNegate() {
        // ignored
    }

    @Override
    public void endNegate() {
        // ignored
    }

    @Override
    public void unknown(RequestPredicate predicate) {
        // ignored
    }

    @Getter
    static class Result {
        private final Set<RequestMethod> requestMethods = new LinkedHashSet<>();
        private final Set<String> paths = new LinkedHashSet<>();
        private final List<HandlerMethod.Parameter> parameters = new ArrayList<>();
        private final Set<String> consumesContentTypesFromHeader = new LinkedHashSet<>();
        private final Set<String> producesContentTypesFromHeader = new LinkedHashSet<>();
    }
}
