/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: UI
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

package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.util.OpenApiConfigurationPropertiesUtils.ConfigurationPropertyCondition;
import de.qaware.openapigeneratorforspring.common.util.OpenApiConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger UI specific properties.
 */
@ConfigurationProperties(prefix = OpenApiConstants.CONFIG_PROPERTIES_PREFIX + ".ui")
@Getter
@Setter
public class OpenApiSwaggerUiConfigurationProperties {
    /**
     * Flag if exposing Swagger UI is enabled. Default true.
     */
    private boolean enabled = true;
    /**
     * Path to the Swagger UI.
     */
    @SuppressWarnings("java:S1075") // suppress hard coded URL warning
    private String path = "/swagger-ui";
    /**
     * Enable caching of the Swagger UI resources.
     */
    private boolean cacheUiResources = false;

    public static class EnabledCondition extends ConfigurationPropertyCondition<OpenApiSwaggerUiConfigurationProperties> {
        public EnabledCondition() {
            super(OpenApiSwaggerUiConfigurationProperties.class, OpenApiSwaggerUiConfigurationProperties::isEnabled);
        }
    }
}
