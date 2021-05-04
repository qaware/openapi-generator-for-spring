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

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Supplier for Jackson's object mapper.
 */
@FunctionalInterface
public interface OpenApiObjectMapperSupplier {
    /**
     * Get the object mapper. During OpenApi building, this might be called many times!
     *
     * @param purpose for which the Object Mapper is going to be used
     * @return object mapper
     */
    ObjectMapper get(Purpose purpose);

    /**
     * Purpose.
     */
    enum Purpose {
        /**
         * For serializing the Open Api model into JSON.
         */
        OPEN_API_JSON,
        /**
         * For analyzing types during Schema building.
         */
        SCHEMA_BUILDING,
        /**
         * For parsing values from annotation strings.
         */
        PARSABLE_VALUE_MAPPER
    }
}
