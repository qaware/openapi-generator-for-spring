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

package de.qaware.openapigeneratorforspring.common.schema.resolver;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * {@link Schema} resolver including internal
 * referencing of possibly nested schemas.
 */
public interface SchemaResolver {
    /**
     * Resolve from given java type using the given
     * annotations supplier. The finally built top-level
     * schema will also be "maybe referenced" if not empty.
     *
     * @param type                     java type (Jackson type will be constructed from it)
     * @param annotationsSupplier      annotations supplier
     * @param referencedSchemaConsumer referenced schema consumer for nested schemas
     * @param schemaSetter             schema setter (consumes the result if resolved schema is not empty)
     */
    void resolveFromType(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer, Consumer<Schema> schemaSetter);

    /**
     * Resolve from given java class without referencing
     * the top-level schema, which is returned.
     * Annotations will be used from the given class.
     *
     * @param clazz                    java clazz (Jackson type will be constructed from it)
     * @param referencedSchemaConsumer referenced schema consumer for nested schemas
     * @return resolved schema, might be empty if input is Void.class
     */
    Schema resolveFromClassWithoutReference(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer);
}
