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

package de.qaware.openapigeneratorforspring.ui.webjar;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class WebJarResourceTransformerSupport {
    private final List<WebJarResourceTransformer> transformers;
    private final WebJarTransformedResourceBuilder transformedResourceBuilder;

    public <E extends Throwable> Resource transformResourceIfMatching(
            Resource resource,
            ResourceContentSupplier<E> resourceContentSupplier
    ) throws E {
        return transformResourceIfMatching(resource, x -> x, (transformer, resourceBuilder) ->
                transformer.andThen(resourceBuilder).apply(resourceContentSupplier.get())
        );
    }

    public <R, E extends Throwable> R transformResourceIfMatching(
            Resource resource,
            Function<Resource, R> resourceTransformer,
            TransformerApplier<R, E> transformerApplier
    ) throws E {
        for (WebJarResourceTransformer transformer : transformers) {
            if (transformer.matches(resource)) {
                return transformerApplier.apply(transformer,
                        transformedContent -> transformedResourceBuilder.buildResource(resource, transformedContent)
                );
            }
        }
        return resourceTransformer.apply(resource);
    }

    @FunctionalInterface
    public interface ResourceContentSupplier<E extends Throwable> {
        String get() throws E;
    }

    @FunctionalInterface
    public interface TransformerApplier<R, E extends Throwable> {
        R apply(WebJarResourceTransformer transformer, Function<String, Resource> resourceBuilder) throws E;
    }
}
