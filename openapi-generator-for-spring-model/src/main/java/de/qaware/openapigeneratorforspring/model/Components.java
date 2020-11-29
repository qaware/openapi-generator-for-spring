/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Model
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

package de.qaware.openapigeneratorforspring.model;

import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.header.Header;
import de.qaware.openapigeneratorforspring.model.link.Link;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import de.qaware.openapigeneratorforspring.model.operation.Callback;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.Data;

import java.util.Map;

/**
 * Components
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#componentsObject"
 */
@Data
public class Components implements HasExtensions, HasIsEmpty<Components> {
    private Map<String, ? extends HasReference<Example>> examples;
    private Map<String, ? extends HasReference<Header>> headers;
    private Map<String, ? extends HasReference<Parameter>> parameters;
    private Map<String, ? extends HasReference<RequestBody>> requestBodies;
    private Map<String, ? extends HasReference<ApiResponse>> responses;
    private Map<String, ? extends HasReference<Schema>> schemas;
    private Map<String, ? extends HasReference<SecurityScheme>> securitySchemes;
    private Map<String, ? extends HasReference<Link>> links;
    private Map<String, ? extends HasReference<Callback>> callbacks;
    private Map<String, Object> extensions;

    @Override
    public Components createInstance() {
        return new Components();
    }
}
