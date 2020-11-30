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
 * Initial type builder. They are queried in {@link org.springframework.core.Ordered order} by the {@link
 * de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver
 * schema resolver} to start schema resolution.
 */
@FunctionalInterface
public interface InitialTypeBuilder extends OpenApiOrderedUtils.DefaultOrdered {
    @Nullable
    InitialType build(JavaType javaType, AnnotationsSupplier annotationsSupplier, FallbackBuilder fallbackBuilder);

    @FunctionalInterface
    interface FallbackBuilder {
        @Nullable
        InitialType build(JavaType javaType, AnnotationsSupplier annotationsSupplier);
    }
}
