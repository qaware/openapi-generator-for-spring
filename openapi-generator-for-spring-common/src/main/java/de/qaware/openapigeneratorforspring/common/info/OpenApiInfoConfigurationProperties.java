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

package de.qaware.openapigeneratorforspring.common.info;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.annotation.Nullable;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;

/**
 * Configuration properties for the OpenApi information.
 * <p>
 * Always take precedence over annotations and other
 * inferred information when building the specification.
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#infoObject"
 */
@ConfigurationProperties(CONFIG_PROPERTIES_PREFIX + ".info")
@Getter
@Setter
public class OpenApiInfoConfigurationProperties {
    @Nullable
    private String title;
    @Nullable
    private String description;
    @Nullable
    private String termsOfService;
    @Nullable
    @NestedConfigurationProperty
    private Contact contact;
    @Nullable
    @NestedConfigurationProperty
    private License license;
    @Nullable
    private String version;
    @Nullable
    private Map<String, Object> extensions;

    @Getter
    @Setter
    public static class Contact {
        @Nullable
        private String name;
        @Nullable
        private String url;
        @Nullable
        private String email;
        @Nullable
        private Map<String, Object> extensions;
    }

    @Getter
    @Setter
    public static class License {
        @Nullable
        private String name;
        @Nullable
        private String url;
        @Nullable
        private Map<String, Object> extensions;
    }
}
