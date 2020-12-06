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

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.web.OpenApiResource;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Hidden       // exclude from OpenApi spec
@ResponseBody // all handler methods return response bodies, not view names
public class OpenApiResourceForWebFlux {

    /**
     * Using this thread local only works if the OpenApi model is built within
     * one thread (or the thread local state must be copied into possible
     * child threads).
     *
     * <p> This should be considered a workaround as WebFlux doesn't
     * offer good alternatives to {@code @RequestScope} beans (yet?).
     */
    static final ThreadLocal<ServerHttpRequest> SERVER_HTTP_REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    private final OpenApiResource openApiResource;

    public <T> void registerMapping(OpenApiResource.RequestMappingInfoBuilder<T> requestMappingInfoBuilder,
                                    OpenApiResource.RequestMappingRegistrar<T> requestMappingRegistrar) {
        openApiResource.registerMapping(requestMappingInfoBuilder, requestMappingRegistrar, this);
    }

    @SuppressWarnings("unused") // is used via reflection in registerMapping
    public String getOpenApiAsJson(ServerHttpRequest serverHttpRequest) throws JsonProcessingException {
        SERVER_HTTP_REQUEST_THREAD_LOCAL.set(serverHttpRequest);
        return openApiResource.getOpenApiAsJson();
    }

    @SuppressWarnings("unused") // is used via reflection in registerMapping
    public String getOpenApiAsYaml(ServerHttpRequest serverHttpRequest) throws OpenApiResource.OpenApiYamlNotSupportedException, JsonProcessingException {
        SERVER_HTTP_REQUEST_THREAD_LOCAL.set(serverHttpRequest);
        return openApiResource.getOpenApiAsYaml();
    }
}
