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

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.net.URI;

@RequiredArgsConstructor
public class SwaggerUiIndexHtmlWebJarResourceTransformerFactory implements WebJarResourceTransformerFactory {

    private final OpenApiConfigurationProperties properties;
    private final OpenApiSwaggerUiApiDocsUrisSupplier swaggerUiApiDocsUrisSupplier;

    @Override
    public WebJarResourceTransformer create(URI baseUri, @Nullable OpenApiSwaggerUiCsrfSupport csrfSupport) {
        return new SwaggerUiIndexHtmlWebJarResourceTransformer(baseUri, csrfSupport, properties, swaggerUiApiDocsUrisSupplier);
    }
}
