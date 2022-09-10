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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * {@link Schema} resolver including internal
 * referencing of possibly nested schemas.
 */
public interface SchemaResolver {
    /**
     * Resolve from given java type using the given
     * AnnotationsSupplier. The finally built top-level
     * schema will also be "maybe referenced" if not empty.
     *
     * @param caller                   resolver mode (serialization or deserialization)
     * @param type                     java type (Jackson type will be constructed from it)
     * @param annotationsSupplier      annotations supplier
     * @param referencedSchemaConsumer referenced schema consumer for nested schemas
     * @param schemaSetter             schema setter (consumes the result if resolved schema is not empty)
     */
    void resolveFromType(Caller caller, Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer, Consumer<Schema> schemaSetter);

    /**
     * Resolve from given java class without referencing
     * the top-level schema, which is returned.
     * Annotations will be used from the given class.
     *
     * @param caller                   resolver mode (serialization or deserialization)
     * @param clazz                    java clazz (Jackson type will be constructed from it)
     * @param referencedSchemaConsumer referenced schema consumer for nested schemas
     * @return resolved schema, might be empty if input is {@code Void.class}
     */
    Schema resolveFromClassWithoutReference(Caller caller, Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer);

    /**
     * The caller of the {@link SchemaResolver}.
     * <p>
     * Instances of this class are usually singletons (see also  and are useful to detect
     * for which part of the OpenApi spec the current schema resolution is done.
     * <p>
     * {@code @EqualsAndHashCode} is left out intentionally as singleton instances should be compared
     */
    @RequiredArgsConstructor(staticName = "of")
    @Getter
    @ToString
    class Caller {
        public static final Caller API_RESPONSE = Caller.of(Mode.FOR_SERIALIZATION);
        public static final Caller PARAMETER = Caller.of(Mode.FOR_DESERIALIZATION);
        public static final Caller REQUEST_BODY = Caller.of(Mode.FOR_DESERIALIZATION);
        public static final Caller HEADER = Caller.of(Mode.FOR_DESERIALIZATION);

        /**
         * The {@link Mode} for the schema resolution, provided by the caller.
         */
        private final Mode mode;
    }

    /**
     * Schema Resolver Mode. Schemas might resolve differently
     * if they about to be serialized (= returned to client)
     * or are deserialized (= consumed from client).
     */
    enum Mode {
        /**
         * Schema is used to describe something
         * which is consumed from the client.
         */
        FOR_DESERIALIZATION,
        /**
         * Schema is used to describe something
         * which is returned to client.
         */
        FOR_SERIALIZATION
    }
}
