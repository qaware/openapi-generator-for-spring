/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
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

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier.Purpose.OPEN_API_JSON;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.OPEN_API_DOCS_DEFAULT_PATH;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Hidden
public abstract class AbstractOpenApiResource {

    protected static final String API_DOCS_PATH_SPEL = "${" +
            // "api-docs-path" should match OpenApiConfigurationProperties setting
            CONFIG_PROPERTIES_PREFIX + ".api-docs-path:" +
            OPEN_API_DOCS_DEFAULT_PATH + "}";

    private final OpenApiGenerator openApiGenerator;
    private final OpenApiObjectMapperSupplier objectMapperSupplier;

    protected String getOpenApiAsJson() throws JsonProcessingException {
        OpenApi openApi = openApiGenerator.generateOpenApi();
        return objectMapperSupplier.get(OPEN_API_JSON).writeValueAsString(openApi);
    }
}
