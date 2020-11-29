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

package de.qaware.openapigeneratorforspring.ui.swagger;

import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.webjar.ShadedWebJarAssetLocator;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ResourceUtils;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@RequiredArgsConstructor
public class SwaggerUiSupport {
    static final String INDEX_HTML_FILE = "index.html";
    private static final String SWAGGER_UI_WEB_JAR = "swagger-ui";

    private final OpenApiSwaggerUiConfigurationProperties swaggerUiProperties;

    public String getWebJarClassPath() {
        String pathToSwaggerUiIndexHtml = new ShadedWebJarAssetLocator().getFullPath(SWAGGER_UI_WEB_JAR, INDEX_HTML_FILE);
        String pathToSwaggerUi = pathToSwaggerUiIndexHtml.substring(0, pathToSwaggerUiIndexHtml.length() - INDEX_HTML_FILE.length());
        return ResourceUtils.CLASSPATH_URL_PREFIX + pathToSwaggerUi;
    }

    public String getUiPath() {
        return swaggerUiProperties.getPath();
    }

    public String getRedirectPath() {
        return swaggerUiProperties.getPath() + DEFAULT_PATH_SEPARATOR + INDEX_HTML_FILE;
    }
}
