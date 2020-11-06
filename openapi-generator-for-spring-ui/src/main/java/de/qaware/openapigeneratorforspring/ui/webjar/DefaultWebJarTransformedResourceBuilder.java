package de.qaware.openapigeneratorforspring.ui.webjar;

import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiConfigurationProperties;
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
                        () -> nowInstantSupplier.get().toEpochMilli()
        );
    }

    @Getter
    private static class StringResourceWithLastModified extends ByteArrayResource {
        private final LastModifiedSupplier lastModifiedSupplier;

        public StringResourceWithLastModified(String content, LastModifiedSupplier lastModifiedSupplier) {
            super(content.getBytes());
            this.lastModifiedSupplier = lastModifiedSupplier;
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
