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

package de.qaware.openapigeneratorforspring.common.supplier;

import java.net.URI;

/**
 * Bean providing the Open Api Base URI, which works
 * even for WebFlux if called within OpenApi building.
 */
@FunctionalInterface
public interface OpenApiBaseUriSupplier {
    /**
     * Get Base URI. Might throw if called outside Open Api building.
     *
     * @return Base URI to Open Api Resource endpoint.
     */
    URI getBaseUri();
}
