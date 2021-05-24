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

package de.qaware.openapigeneratorforspring.model.path;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.server.Server;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * PathItem
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#pathItemObject"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PathItem implements HasExtensions {
    private String summary;
    private String description;
    private List<Server> servers;
    private List<Parameter> parameters;

    @JsonIgnore // merged into extensions
    @Builder.Default
    private Map<String, Operation> operations = new LinkedHashMap<>();
    private Map<String, Object> extensions;

    // jackson supports only one @JsonAnyGetter, so we merge operations and extensions here
    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> nonNullExtensions = this.extensions == null ? Collections.emptyMap() : this.extensions;
        return Stream.concat(operations.entrySet().stream(), nonNullExtensions.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
                    throw new IllegalStateException("Conflicting key for PathItem property: " + a + "vs. " + b);
                }, LinkedHashMap::new));
    }

    public void setOperation(RequestMethod requestMethod, Operation operation) {
        operations.put(requestMethod.name().toLowerCase(), operation);
    }
}
