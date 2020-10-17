package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.link.Link;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedLinksHandlerImpl implements ReferencedComponentHandler, ReferencedLinksConsumer {

    private final ReferencedLinkStorage storage;

    @Override
    public void maybeAsReference(Map<String, Link> links, Consumer<Map<String, Link>> setter) {
        setter.accept(links);
        storage.storeMaybeReferenceLinks(links);
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setLinks);
    }
}
