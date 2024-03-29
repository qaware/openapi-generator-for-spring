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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Generic type resolver for {@link SchemaResolver}. Enables full
 * control the recursive resolution of {@link Schema schemas}
 * and is useful for generic container-like types such as {@link
 * java.util.Collection collections} and schemas carrying properties.
 *
 * <p>Prefer using {@link InitialSchemaBuilder} for
 * extending schema resolution whenever possible.
 *
 * <p>Each implementation typically has a corresponding
 * {@link InitialSchemaBuilder} as a starting point.
 */
@FunctionalInterface
public interface TypeResolver extends OpenApiOrderedUtils.DefaultOrdered {

    /**
     * Resolve the given initial type and modify the given schema accordingly.
     *
     * @param caller                caller of the schema resolver
     * @param schema                schema to be modified
     * @param initialType           initial type
     * @param schemaBuilderFromType recursive schema builder
     * @return recursion key, or null if no recursion is necessary
     */
    @Nullable
    RecursionKey resolve(SchemaResolver.Caller caller, Schema schema, InitialType initialType, SchemaBuilderFromType schemaBuilderFromType);

    /**
     * Helper interface for {@link #resolve}. Represents a
     * unique key identifying the built schema, without having
     * the obligation to completely construct the final schema.
     */
    interface RecursionKey {

    }

    /**
     * Helper interface for {@link #resolve}.
     *
     * <p>Note that the provided schema consumers might be
     * called several times during recursive schema building.
     */
    interface SchemaBuilderFromType {
        /**
         * Recursively build the nested schema from the given type.
         *
         * @param javaType            nested java type
         * @param annotationsSupplier annotations supplier for type
         * @param schemaConsumer      consumer for schema
         */
        void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer);

        /**
         * Identical to {@link #buildSchemaFromType} but
         * ensures that the schema provided to the given schema
         * consumer will have a reference set eventually.
         *
         * @param javaType            nested java type
         * @param annotationsSupplier annotations supplier for type
         * @param schemaConsumer      consumer for schema, {@link Schema#getRef()} will eventually return non-empty string
         */
        void buildSchemaReferenceFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer);
    }
}
