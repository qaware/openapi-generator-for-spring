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
