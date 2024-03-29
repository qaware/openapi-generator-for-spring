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

package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;

import java.util.Map;

/**
 * Properties resolver for Jackson type.
 *
 * <p>Prefer using {@link SchemaPropertyFilter} whenever possible.
 */
public interface SchemaPropertiesResolver extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Find properties for given java type.
     *
     * @param caller              caller of the schema resolver
     * @param javaType            Jackson java type
     * @param annotationsSupplier annotations supplier for given java type
     * @return map of properties (key is property name)
     */
    Map<String, SchemaProperty> findProperties(SchemaResolver.Caller caller, JavaType javaType, AnnotationsSupplier annotationsSupplier);
}
