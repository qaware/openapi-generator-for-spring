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

package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Factory for {@link SchemaAnnotationMapper}. Resolved circular bean
 * dependency of {@link SchemaResolver} and {@link SchemaAnnotationMapper},
 * as during mapping of the {@link io.swagger.v3.oas.annotations.media.Schema
 * schema annotation}, nested schema resolution may also happen (for example
 * for {@link Schema#discriminatorMapping() discriminator mappings}).
 */
public interface SchemaAnnotationMapperFactory {
    SchemaAnnotationMapper create(SchemaResolver schemaResolver);
}
