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

package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.stream.Stream;

public class SpringWebHandlerMethodRequestBodyParameterMapper {

    public Optional<RequestBodyParameter> findRequestBodyParameter(SpringWebHandlerMethod handlerMethod) {
        return handlerMethod.getParameters().stream()
                .flatMap(parameter -> parameter.getAnnotationsSupplier()
                        .findAnnotations(RequestBody.class)
                        .findFirst()
                        .map(RequestBody::required)
                        .map(requiredFlag -> RequestBodyParameter.of(parameter, requiredFlag))
                        .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
                )
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one parameter marked with @RequestBody on " + this);
                });
    }

    @RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    @Getter
    public static class RequestBodyParameter {
        private final HandlerMethod.Parameter parameter;
        private final boolean required;
    }
}
