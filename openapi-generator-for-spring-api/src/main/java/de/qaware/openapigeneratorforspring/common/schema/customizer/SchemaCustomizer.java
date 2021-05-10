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

package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.media.Schema;

/**
 * Customizer for {@link Schema}. Run AFTER the initial schema is built
 * and annotations are applied, but BEFORE nested properties are resolved.
 *
 * @see SchemaPropertiesCustomizer for customizing properties
 */
@FunctionalInterface
public interface SchemaCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize the given schema by reference.
     *
     * @param schema                   schema
     * @param javaType                 java type this schema was built from (might be Void)
     * @param annotationsSupplier      annotations supplier used during schema building
     * @param schemaResolver           schema resolver to create a new schema
     * @param mode                     the mode of the schema creation
     * @param referencedSchemaConsumer referenced schema consumer for nested schemas
     */
    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaResolver schemaResolver, SchemaResolver.Mode mode, ReferencedSchemaConsumer referencedSchemaConsumer);
}
