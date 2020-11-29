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

package de.qaware.openapigeneratorforspring.model.security;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * SecurityRequirement
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#securityRequirementObject"
 */
public class SecurityRequirement extends LinkedHashMap<String, List<String>> {

    public SecurityRequirement require(String securitySchemeName, String... scopes) {
        put(securitySchemeName, Arrays.asList(scopes));
        return this; // fluent
    }

}

