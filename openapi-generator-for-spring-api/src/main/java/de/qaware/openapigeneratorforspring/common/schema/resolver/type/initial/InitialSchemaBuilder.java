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
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;

/**
 * Initial schema builder. Build the schema
 * determined from {@link InitialTypeBuilder}.
 *
 * <p>They are queried in {@link
 * org.springframework.core.Ordered order} by the {@link
 * de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver
 * schema resolver} to initiate schema resolution.
 */
@FunctionalInterface
public interface InitialSchemaBuilder extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Build initial {@link Schema} from given java type.
     * Can return null if that java type cannot be handled.
     *
     * @param javaType java type
     * @return initial schema, or null if java type cannot be handled
     */
    @Nullable
    Schema buildFromType(JavaType javaType);
}
