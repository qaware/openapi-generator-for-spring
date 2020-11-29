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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouterFunctionHandlerMethodMapper {

    public static class RequestBodyMapper implements HandlerMethod.RequestBodyMapper {
        @Nullable
        @Override
        public List<HandlerMethod.RequestBody> map(HandlerMethod handlerMethod) {
            // TODO implement when dummy parameter type is seen on non-empty content type header?
            return Collections.emptyList();
        }
    }

    public static class ResponseMapper implements HandlerMethod.ResponseMapper {
        @Nullable
        @Override
        public List<HandlerMethod.Response> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof RouterFunctionHandlerMethod) {
                RouterFunctionHandlerMethod routerFunctionHandlerMethod = (RouterFunctionHandlerMethod) handlerMethod;
                RouterFunctionHandlerMethod.Response response = new RouterFunctionHandlerMethod.Response(
                        // TODO check if response code just from bean factory method is good enough
                        findResponseCode(handlerMethod),
                        routerFunctionHandlerMethod.getRouterFunctionAnalysisResult().getProducesContentTypesFromHeader(),
                        // Schema building could still use @Schema annotation from bean factory method,
                        // so supply a "dummy" type here for schema building
                        new RouterFunctionHandlerMethod.RouterFunctionType(AnnotationsSupplier.EMPTY, Void.class)
                );
                return Collections.singletonList(response);
            }
            return null;
        }

        private static String findResponseCode(HandlerMethod handlerMethod) {
            return handlerMethod.findAnnotations(ResponseStatus.class)
                    .map(ResponseStatus::code)
                    .mapToInt(HttpStatus::value)
                    .mapToObj(Integer::toString)
                    .findFirst()
                    .orElseGet(() -> Integer.toString(HttpStatus.OK.value()));
        }
    }
}
