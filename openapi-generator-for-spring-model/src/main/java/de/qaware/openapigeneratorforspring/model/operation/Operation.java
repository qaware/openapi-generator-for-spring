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

package de.qaware.openapigeneratorforspring.model.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;
import de.qaware.openapigeneratorforspring.model.server.Server;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Operation
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#operationObject"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Operation implements HasExtensions {
    private List<String> tags;
    private String summary;
    private String description;
    private ExternalDocumentation externalDocs;
    private String operationId;
    private List<Parameter> parameters;
    private RequestBody requestBody;
    private ApiResponses responses;
    private Map<String, Callback> callbacks;
    private Boolean deprecated;
    private List<SecurityRequirement> security;
    private List<Server> servers;
    private Map<String, Object> extensions;

    @JsonIgnore
    public Optional<SecurityRequirement> getFirstSecurity() {
        return Optional.ofNullable(security)
                .map(Collection::stream)
                .flatMap(Stream::findFirst);
    }

    public void setFirstSecurity(SecurityRequirement securityRequirement) {
        if (security == null) {
            security = new ArrayList<>();
        }
        if (!security.isEmpty()) {
            security.set(0, securityRequirement);
        }
        security.add(securityRequirement);
    }
}

