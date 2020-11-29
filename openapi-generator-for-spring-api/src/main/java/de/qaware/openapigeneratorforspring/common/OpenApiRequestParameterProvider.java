/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
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

package de.qaware.openapigeneratorforspring.common;

import java.util.List;
import java.util.Optional;

public interface OpenApiRequestParameterProvider {

    List<String> getHeaderValues(String headerName);

    default Optional<String> getFirstHeaderValue(String headerName) {
        return getHeaderValues(headerName).stream().findFirst();
    }

    List<String> getQueryParameters(String parameterName);

    default Optional<String> getFirstQueryParameter(String parameterName) {
        return getQueryParameters(parameterName).stream().findFirst();
    }
}
