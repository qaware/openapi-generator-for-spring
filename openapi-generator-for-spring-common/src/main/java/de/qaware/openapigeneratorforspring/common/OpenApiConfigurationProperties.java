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

package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.util.OpenApiConfigurationPropertiesUtils.ConfigurationPropertyCondition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.OPEN_API_DOCS_DEFAULT_PATH;

@ConfigurationProperties(prefix = CONFIG_PROPERTIES_PREFIX)
@Getter
@Setter
public class OpenApiConfigurationProperties {
    /**
     * Flag to provide the OpenAPI spec as an accessible endpoint.
     */
    private boolean enabled = true;
    /**
     * Path to OpenAPI spec. Default is <code>/v3/api-docs</code>.
     */
    private String apiDocsPath = OPEN_API_DOCS_DEFAULT_PATH;

    public static class EnabledCondition extends ConfigurationPropertyCondition<OpenApiConfigurationProperties> {
        public EnabledCondition() {
            super(OpenApiConfigurationProperties.class, OpenApiConfigurationProperties::isEnabled);
        }
    }
}
