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

package de.qaware.openapigeneratorforspring.ui.webflux;

import de.qaware.openapigeneratorforspring.ui.swagger.OpenApiSwaggerUiCsrfSupport;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerSupport;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarTransformedResourceBuilder;
import de.qaware.openapigeneratorforspring.webflux.OpenApiBaseUriSupplierForWebFlux;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
public class WebJarResourceTransformerSupportFactoryForWebFlux {

    private final List<WebJarResourceTransformerFactory> resourceTransformerFactories;
    private final WebJarTransformedResourceBuilder transformedResourceBuilder;
    @Nullable
    private final OpenApiSwaggerUiCsrfSupportProviderForWebFlux openApiSwaggerUiCsrfSupportProviderForWebFlux;

    public Mono<WebJarResourceTransformerSupport> create(ServerWebExchange exchange) {
        URI baseUri = OpenApiBaseUriSupplierForWebFlux.getBaseUri(exchange);
        return getCsrfSupport(exchange)
                .map(csrfSupport -> new WebJarResourceTransformerSupport(createTransformers(baseUri, csrfSupport), transformedResourceBuilder))
                .switchIfEmpty(Mono.fromSupplier(() -> new WebJarResourceTransformerSupport(createTransformers(baseUri, null), transformedResourceBuilder)));
    }

    private List<WebJarResourceTransformer> createTransformers(URI baseUri, OpenApiSwaggerUiCsrfSupport csrfSupport) {
        return resourceTransformerFactories.stream()
                .map(factory -> factory.create(baseUri, csrfSupport))
                .toList();
    }

    private Mono<OpenApiSwaggerUiCsrfSupport> getCsrfSupport(ServerWebExchange exchange) {
        if (openApiSwaggerUiCsrfSupportProviderForWebFlux == null) {
            return Mono.empty();
        }
        return openApiSwaggerUiCsrfSupportProviderForWebFlux.getCsrfSupport(exchange);
    }
}
