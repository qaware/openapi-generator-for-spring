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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class SpringWebHandlerMethodRequestBodyParameterMapper {

    public Optional<RequestBodyParameter> findRequestBodyParameter(SpringWebHandlerMethod handlerMethod) {
        return handlerMethod.getParameters().stream()
                .flatMap(parameter ->
                        Stream.concat(
                                parameter.getAnnotationsSupplier()
                                        .findAnnotations(RequestBody.class)
                                        .map(RequestBody::required)
                                        .map(requiredFlag -> RequestBodyParameter.of(parameter, requiredFlag)),
                                // also check if we're encountering a "bare" InputStream as a parameter
                                // this can also be seen as request body
                                parameter.getType()
                                        .filter(type -> type.getType().equals(InputStream.class))
                                        .map(type -> RequestBodyParameter.of(parameter, false))
                                        .map(Stream::of).orElseGet(Stream::empty)
                        )
                )
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one parameter as a request body candidate on " + handlerMethod);
                });
    }

    @RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    @Getter
    public static class RequestBodyParameter {
        private final HandlerMethod.Parameter parameter;
        private final boolean required;
    }
}
