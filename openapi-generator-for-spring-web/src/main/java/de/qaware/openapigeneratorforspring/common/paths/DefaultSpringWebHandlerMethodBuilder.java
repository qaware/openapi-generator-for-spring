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

package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.parameter.OpenApiSpringWebParameterNameDiscoverer;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class DefaultSpringWebHandlerMethodBuilder implements SpringWebHandlerMethodBuilder {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final OpenApiSpringWebParameterNameDiscoverer parameterNameDiscoverer;

    @Override
    public HandlerMethod build(org.springframework.web.method.HandlerMethod springWebHandlerMethod) {
        Method method = springWebHandlerMethod.getMethod();
        return new SpringWebHandlerMethod(
                annotationsSupplierFactory.createFromMethodWithDeclaringClass(method),
                buildParameters(springWebHandlerMethod),
                springWebHandlerMethod
        );
    }

    private List<HandlerMethod.Parameter> buildParameters(org.springframework.web.method.HandlerMethod springWebHandlerMethod) {
        MethodParameter[] methodParameters = springWebHandlerMethod.getMethodParameters();
        IntFunction<String> parameterNameSupplier = buildParameterNameSupplier(springWebHandlerMethod);
        return IntStream.range(0, methodParameters.length).boxed()
                .map(parameterIndex -> buildParameter(methodParameters[parameterIndex], parameterNameSupplier.apply(parameterIndex)))
                .collect(Collectors.toList());
    }

    private IntFunction<String> buildParameterNameSupplier(org.springframework.web.method.HandlerMethod springWebHandlerMethod) {
        List<String> parameterNames = parameterNameDiscoverer.getParameterNames(springWebHandlerMethod.getMethod());
        if (parameterNames != null) {
            int numberOfMethodParameters = springWebHandlerMethod.getMethodParameters().length;
            if (parameterNames.size() == numberOfMethodParameters) {
                return parameterNames::get;
            }
            LOGGER.warn("Handler method {} has {} parameters but mismatching {} parameter names {} discovered, will ignore parameter names",
                    springWebHandlerMethod, numberOfMethodParameters, parameterNames.size(), parameterNames
            );
        }
        return parameterIndex -> null;
    }

    private AbstractSpringWebHandlerMethod.SpringWebParameter buildParameter(MethodParameter parameter, @Nullable String parameterName) {
        return new AbstractSpringWebHandlerMethod.SpringWebParameter(
                // simply using parameter.getParameterName() does not work because
                // we don't want to set a ParameterNameDiscoverer, which is not thread-safe (see parameter.getParameterName())!
                parameterName,
                AbstractSpringWebHandlerMethod.SpringWebType.of(parameter.getGenericParameterType(), annotationsSupplierFactory.createFromAnnotatedElement(parameter.getParameterType())),
                new AnnotationsSupplier() {
                    @Override
                    public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                        return Optional.ofNullable(parameter.getParameterAnnotation(annotationType))
                                .map(Stream::of).orElseGet(Stream::empty); // Optional.stream()
                    }
                }
        );
    }
}
