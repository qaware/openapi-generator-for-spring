package de.qaware.openapigeneratorforspring.ui.webjar;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class WebJarResourceTransformerSupport {
    private final List<WebJarResourceTransformer> transformers;

    public <E extends Throwable> Resource transformResourceIfMatching(
            Resource resource,
            TransformerApplier<Resource, E> transformerApplier
    ) throws E {
        return transformResourceIfMatching(resource, x -> x, transformerApplier);
    }

    public <R, E extends Throwable> R transformResourceIfMatching(
            Resource resource,
            Function<Resource, R> untransformedResource,
            TransformerApplier<R, E> transformerApplier
    ) throws E {
        for (WebJarResourceTransformer transformer : transformers) {
            if (transformer.matches(resource)) {
                return transformerApplier.apply(transformer);
            }
        }
        return untransformedResource.apply(resource);
    }

    public interface TransformerApplier<R, E extends Throwable> {
        R apply(WebJarResourceTransformer transformer) throws E;
    }
}
