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

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.AbstractOpenApiResource;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiResourceForWebMvc extends AbstractOpenApiResource {

    public OpenApiResourceForWebMvc(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier objectMapperSupplier) {
        super(openApiGenerator, objectMapperSupplier);
    }

    @Override
    @GetMapping(value = API_DOCS_PATH_SPEL, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOpenApiAsJson() throws JsonProcessingException {
        return super.getOpenApiAsJson();
    }
}
