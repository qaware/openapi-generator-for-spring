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

package de.qaware.openapigeneratorforspring.common.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier.Purpose.PARSABLE_VALUE_MAPPER;

@RequiredArgsConstructor
public class DefaultParsableValueMapper implements ParsableValueMapper {

    private final OpenApiObjectMapperSupplier objectMapperSupplier;

    @Override
    public Object parse(String value) {
        try {
            return objectMapperSupplier.get(PARSABLE_VALUE_MAPPER).readTree(value);
        } catch (JsonProcessingException e) {
            // not a JSON string, return value as is
            return value;
        }
    }
}
