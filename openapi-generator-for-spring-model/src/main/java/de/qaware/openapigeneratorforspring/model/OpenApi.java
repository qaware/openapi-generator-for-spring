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

import de.qaware.openapigeneratorforspring.model.info.Info;
import de.qaware.openapigeneratorforspring.model.path.Paths;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;
import de.qaware.openapigeneratorforspring.model.server.Server;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * OpenAPI
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md"
 */
@Data
@SuppressWarnings("java:S1700")
public class OpenApi implements HasExtensions {
    private String openapi = "3.0.1";
    private Info info;
    private ExternalDocumentation externalDocs;
    private List<Server> servers;
    private List<SecurityRequirement> security;
    private List<Tag> tags;
    private Paths paths;
    private Components components;
    private Map<String, Object> extensions;
}

