package de.qaware.openapigeneratorforspring.common.operation.mimetype;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MimeType;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultProducesMimeTypeProviderStrategy implements ProducesMimeTypeProviderStrategy {

    private final List<ProducesMimeTypeProvider> providers;

    @Override
    public Set<MimeType> getProducesMimeTypes(HandlerMethod handlerMethod) {
        return providers.stream()
                .map(provider -> provider.findProducesMimeTypes(handlerMethod))
                // Produces mime types end up as response body keys in model and must be concrete (aka no wildcards!)
                .map(mimeTypes -> mimeTypes.stream().filter(MimeType::isConcrete).collect(Collectors.toSet()))
                .filter(mimeTypes -> !mimeTypes.isEmpty())
                .findFirst()
                .orElseGet(Collections::emptySet);
    }
}
