/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebMVC
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

package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.emptyListIfNull;

@RequiredArgsConstructor
public class OpenApiRequestAwareSupplierForWebMvc implements OpenApiRequestParameterProvider, OpenApiBaseUriSupplier {

    private final WebRequest webRequest;
    private final HttpServletRequest httpServletRequest;

    @Override
    public URI getBaseUri() {
        // Accessing request-scoped httpServletRequest works here because we're in request scope
        return ServletUriComponentsBuilder.fromContextPath(httpServletRequest).build().toUri();
    }

    @Override
    public List<String> getHeaderValues(String headerName) {
        return emptyListIfNull(webRequest.getHeaderValues(headerName));
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        return emptyListIfNull(webRequest.getParameterValues(parameterName));
    }
}
