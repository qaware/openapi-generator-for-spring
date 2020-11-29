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

import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiConfigurationProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.Instant;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DefaultWebJarTransformedResourceBuilder implements WebJarTransformedResourceBuilder {
    private final OpenApiSwaggerUiConfigurationProperties uiProperties;
    private final Supplier<Instant> nowInstantSupplier;

    @Override
    public Resource buildResource(Resource originalResource, String transformedContent) {
        return new StringResourceWithLastModified(transformedContent,
                uiProperties.isCacheUiResources() ? originalResource::lastModified :
                        () -> nowInstantSupplier.get().toEpochMilli(),
                originalResource.getFilename()
        );
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    private static class StringResourceWithLastModified extends ByteArrayResource {
        private final LastModifiedSupplier lastModifiedSupplier;
        private final String filename; // important for correct content type!

        public StringResourceWithLastModified(String content, LastModifiedSupplier lastModifiedSupplier, String filename) {
            super(content.getBytes());
            this.lastModifiedSupplier = lastModifiedSupplier;
            this.filename = filename;
        }

        @Override
        public long lastModified() throws IOException {
            return lastModifiedSupplier.getLastModified();
        }
    }

    @FunctionalInterface
    private interface LastModifiedSupplier {
        long getLastModified() throws IOException;
    }
}
