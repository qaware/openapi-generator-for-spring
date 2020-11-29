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

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(onlyExplicitlyIncluded = true)
public class RouterFunctionHandlerMethod implements HandlerMethod {

    @ToString.Include
    @Getter
    private final String identifier;
    private final AnnotationsSupplier annotationsSupplier;
    @ToString.Include
    private final RouterFunction<?> routerFunction;
    @Getter
    private final RouterFunctionAnalysis.Result routerFunctionAnalysisResult;

    @Override
    public List<HandlerMethod.Parameter> getParameters() {
        return routerFunctionAnalysisResult.getParameters();
    }

    @Override
    public <A extends Annotation> ContextAwareAnnotations<A> findAnnotationsWithContext(Class<A> annotationType) {
        return () -> annotationsSupplier.findAnnotations(annotationType);
    }

    @RequiredArgsConstructor
    @Getter
    static class RouterFunctionType implements HandlerMethod.Type {
        private final AnnotationsSupplier annotationsSupplier;
        private final java.lang.reflect.Type type;
    }

    @RequiredArgsConstructor
    static class Parameter implements HandlerMethod.Parameter {
        private final String routerParameterName;
        @Getter(AccessLevel.PACKAGE)
        private final ParameterIn parameterIn;

        @Override
        public Optional<String> getName() {
            return Optional.of(routerParameterName);
        }

        @Override
        public AnnotationsSupplier getAnnotationsSupplier() {
            return AnnotationsSupplier.EMPTY;
        }

        @Override
        public Optional<Type> getType() {
            return Optional.empty();
        }
    }

    @RequiredArgsConstructor
    @Getter
    static class Response implements HandlerMethod.Response {
        private final String responseCode;
        private final Set<String> producesContentTypes;
        private final RouterFunctionType routerFunctionType;

        @Override
        public Optional<Type> getType() {
            return Optional.of(routerFunctionType);
        }
    }
}
