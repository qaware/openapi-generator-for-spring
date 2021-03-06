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

package de.qaware.openapigeneratorforspring.common.reference;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReferenceType {

    EXAMPLE("#/components/examples/"),
    HEADER("#/components/headers/"),
    PARAMETER("#/components/parameters/"),
    REQUEST_BODY("#/components/requestBodies/"),
    API_RESPONSE("#/components/responses/"),
    SCHEMA("#/components/schemas/"),
    // security schemes are referenced directly by @SecurityRequirement
    LINK("#/components/links/"),
    CALLBACK("#/components/callbacks/"),
    ;

    @Getter(AccessLevel.PROTECTED)
    private final String referencePrefix;
}
