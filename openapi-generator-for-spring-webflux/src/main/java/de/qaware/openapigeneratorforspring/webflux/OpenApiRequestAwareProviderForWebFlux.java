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

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import static de.qaware.openapigeneratorforspring.webflux.OpenApiResourceForWebFlux.SERVER_HTTP_REQUEST_THREAD_LOCAL;

@RequiredArgsConstructor
public class OpenApiRequestAwareProviderForWebFlux implements OpenApiRequestParameterProvider {


    @Override
    public List<String> getHeaderValues(String headerName) {
        return SERVER_HTTP_REQUEST_THREAD_LOCAL.get().getHeaders().getOrEmpty(headerName);
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        return SERVER_HTTP_REQUEST_THREAD_LOCAL.get().getQueryParams().getOrDefault(parameterName, Collections.emptyList());
    }
}
