package de.qaware.openapigeneratorforspring.common.reference.header;

import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper.HeaderWithOptionalName;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.header.Header;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedHeadersHandlerImpl implements ReferencedComponentHandler, ReferencedHeadersConsumer {

    private final ReferencedHeaderStorage storage;
    private final ReferenceIdentifierFactoryForHeader referenceIdentifierFactory;

    @Override
    public void maybeAsReference(List<HeaderWithOptionalName> headers, Consumer<Map<String, Header>> headersSetter) {
        // if the provided header doesn't have a name,
        // we use the suggested identifier from the factory
        Map<String, Header> headersMap = headers.stream()
                .map(headerWithOptionalName -> Pair.of(
                        getHeaderName(headerWithOptionalName.getName()),
                        headerWithOptionalName.getHeader()
                ))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> {
                    throw new IllegalStateException("Non-unique suggested identifier encountered: " + a + " vs. " + b);
                }));
        headersSetter.accept(headersMap);
        headersMap.forEach((name, header) ->
                storage.storeMaybeReference(name, header, headerReference -> headersMap.put(name, headerReference))
        );
    }

    private String getHeaderName(@Nullable String headerName) {
        return referenceIdentifierFactory.buildSuggestedIdentifier(headerName);
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setHeaders);
    }
}
