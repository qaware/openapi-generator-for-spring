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


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

/**
 * Callback
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#callbackObject"
 */
@EqualsAndHashCode
@ToString
public class Callback implements HasReference<Callback> {

    @JsonIgnore
    @Setter
    private String callbackUrlExpression;
    @JsonIgnore
    @Setter
    private PathItem pathItem;

    @Getter
    @Setter
    private String ref;

    @JsonAnyGetter
    public Map<String, PathItem> get() {
        return Collections.singletonMap(callbackUrlExpression, pathItem);
    }

    @Override
    public Callback createInstance() {
        return new Callback();
    }
}
