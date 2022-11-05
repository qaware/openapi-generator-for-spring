package de.qaware.openapigeneratorforspring.common.operation.mimetype;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MimeType;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultConsumesMimeTypeProviderStrategy implements ConsumesMimeTypeProviderStrategy {

    private final List<ConsumesMimeTypeProvider> providers;

    @Override
    public Set<MimeType> getConsumesMimeTypes(HandlerMethod handlerMethod) {
        return providers.stream()
                .map(provider -> provider.findConsumesMimeTypes(handlerMethod))
                .filter(mimeTypes -> !mimeTypes.isEmpty())
                .findFirst()
                .orElseGet(Collections::emptySet);
    }
}
