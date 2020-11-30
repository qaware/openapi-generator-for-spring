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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;

import javax.annotation.Nullable;

/**
 * Initial type builder.
 *
 * <p>They are queried in {@link org.springframework.core.Ordered order} by the {@link
 * de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver
 * schema resolver} to before {@link InitialSchemaBuilder} are queried.
 */
@FunctionalInterface
public interface InitialTypeBuilder extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Build the initial type for schema resolution, possibly recursively using fallback builder.
     *
     * @param javaType            current java type
     * @param annotationsSupplier annotations supplier for java type
     * @param recursiveBuilder    builder to recursively build type (useful for generic types)
     * @return initial type, or null if given type can't be handled
     */
    @Nullable
    InitialType build(JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveBuilder recursiveBuilder);

    /**
     * Helper for {@link InitialTypeBuilder#build}.
     */
    interface RecursiveBuilder {
        /**
         * Build from Jackson's java type.
         *
         * @param javaType            java type
         * @param annotationsSupplier annotations supplier for type
         * @return initial type, or null if given type can't be handled
         */
        @Nullable
        InitialType build(JavaType javaType, AnnotationsSupplier annotationsSupplier);

        /**
         * Build from plain java type (Jackson will be used to construct the type).
         *
         * @param type                plain java type
         * @param annotationsSupplier annotations supplier for type
         * @return initial type, or null if given type can't be handled
         */
        @Nullable
        InitialType build(java.lang.reflect.Type type, AnnotationsSupplier annotationsSupplier);
    }
}
