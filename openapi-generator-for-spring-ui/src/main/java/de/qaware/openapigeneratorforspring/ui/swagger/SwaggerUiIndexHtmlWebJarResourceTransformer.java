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
import de.qaware.openapigeneratorforspring.ui.mustache.MustacheTemplate;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.ui.swagger.SwaggerUiSupport.INDEX_HTML_FILE;

@RequiredArgsConstructor
public class SwaggerUiIndexHtmlWebJarResourceTransformer implements WebJarResourceTransformer {

    private static final MustacheTemplate INDEX_HTML_MUSTACHE_TEMPLATE = new MustacheTemplate("/swagger-ui/index.html.mustache");

    private final URI baseUri;
    private final OpenApiConfigurationProperties properties;
    private final OpenApiSwaggerUiApiDocsUrisSupplier swaggerUiApiDocsUrisSupplier;

    @Override
    public boolean matches(Resource resource) {
        if (resource instanceof ClassPathResource) {
            ClassPathResource classPathResource = (ClassPathResource) resource;
            return classPathResource.getPath().endsWith(INDEX_HTML_FILE);
        }
        return false;
    }

    @Override
    public String apply(String resourceContent) {
        URI apiDocsUri = UriComponentsBuilder.fromUri(baseUri)
                // .path appends to the base uri path and thus preserves a possible context path
                .path(properties.getApiDocsPath())
                .build().toUri();
        List<OpenApiSwaggerUiApiDocsUrisSupplier.ApiDocsUriWithName> apiDocsUris = swaggerUiApiDocsUrisSupplier.getApiDocsUris(apiDocsUri);
        TemplateContext context = TemplateContext.of(
                apiDocsUris.size() == 1 ? apiDocsUris.iterator().next().getApiDocsUri() : null,
                mapApiDocsUris(apiDocsUris)
        );
        return INDEX_HTML_MUSTACHE_TEMPLATE.execute(context);
    }

    private List<TemplateContext.UrlItem> mapApiDocsUris(List<OpenApiSwaggerUiApiDocsUrisSupplier.ApiDocsUriWithName> apiDocsUris) {
        if (apiDocsUris.size() == 1) {
            return Collections.emptyList();
        }
        return apiDocsUris.stream()
                .map(uri -> TemplateContext.UrlItem.of(uri.getName(), uri.getApiDocsUri()))
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class TemplateContext {
        @Nullable
        private final URI url;
        private final List<UrlItem> urls;

        @RequiredArgsConstructor(staticName = "of")
        @Getter
        private static class UrlItem {
            private final String name;
            private final URI url;
        }
    }
}
