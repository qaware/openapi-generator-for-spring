/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
 * %%
 * Copyright (C) 2020 - 2021 QAware GmbH
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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;

/**
 * Interface allowing to customize the schema name for the type property field.
 *
 * @see de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameBuilder
 */
public interface JacksonPolymorphismTypeSchemaNameBuilder {
    /**
     * Build the schema name (must not be unique).
     *
     * @param javaTypeOwningJsonTypeInfo java type of class where @JsonTypeInfo is located
     * @param jsonTypeInfo               annotation
     * @return schema name for type property field
     */
    String build(JavaType javaTypeOwningJsonTypeInfo, JsonTypeInfo jsonTypeInfo);
}
