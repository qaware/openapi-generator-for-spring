/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;

@ConfigurationProperties(CONFIG_PROPERTIES_PREFIX + ".extension.java8.time")
@Getter
@Setter
public class Java8TimeTypeResolverConfigurationProperties {

    /**
     * Format to use in schema type.
     */
    private Format format = Format.INFER_FROM_OBJECT_MAPPER;

    @RequiredArgsConstructor
    public enum Format {
        INFER_FROM_OBJECT_MAPPER,
        ISO8601,
        UNIX_EPOCH_SECONDS;
    }
}
