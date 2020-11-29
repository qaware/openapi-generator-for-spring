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

package de.qaware.openapigeneratorforspring.ui.webmvc;

import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformer;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerFactory;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarResourceTransformerSupport;
import de.qaware.openapigeneratorforspring.ui.webjar.WebJarTransformedResourceBuilder;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WebJarResourceTransformerSupportFactoryForWebMvc {

    private final List<WebJarResourceTransformerFactory> resourceTransformerFactories;
    private final OpenApiBaseUriSupplier openApiBaseUriSupplier;
    private final WebJarTransformedResourceBuilder transformedResourceBuilder;

    WebJarResourceTransformerSupport create() {
        URI baseUri = openApiBaseUriSupplier.getBaseUri();
        List<WebJarResourceTransformer> transformers = resourceTransformerFactories.stream()
                .map(factory -> factory.create(baseUri))
                .collect(Collectors.toList());
        return new WebJarResourceTransformerSupport(transformers, transformedResourceBuilder);
    }
}
