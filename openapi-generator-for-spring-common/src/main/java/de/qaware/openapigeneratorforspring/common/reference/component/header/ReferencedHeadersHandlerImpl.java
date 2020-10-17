package de.qaware.openapigeneratorforspring.common.reference.component.header;

import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper.HeaderWithOptionalName;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.header.Header;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedHeadersHandlerImpl implements ReferencedComponentHandler, ReferencedHeadersConsumer {

    private final ReferencedHeaderStorage storage;
    private final ReferenceIdentifierFactoryForHeader referenceIdentifierFactory;

    @Override
    public void maybeAsReference(List<HeaderWithOptionalName> headers, Consumer<Map<String, Header>> headersSetter) {
        Map<String, Header> headersMap = OpenApiMapUtils.buildStringMapFromStream(
                headers.stream(),
                this::getHeaderName,
                HeaderWithOptionalName::getHeader
        );
        headersSetter.accept(headersMap);
        headersMap.forEach((name, header) ->
                storage.storeMaybeReference(name, header, headerReference -> headersMap.put(name, headerReference))
        );
    }

    private String getHeaderName(HeaderWithOptionalName headerWithOptionalName) {
        // if the provided header doesn't have a name,
        // we use the suggested identifier from the factory
        return referenceIdentifierFactory.buildSuggestedIdentifier(headerWithOptionalName.getName());
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setHeaders);
    }
}
