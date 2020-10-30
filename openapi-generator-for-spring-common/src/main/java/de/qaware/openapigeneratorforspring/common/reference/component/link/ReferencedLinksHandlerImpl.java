package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.handler.DependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.link.Link;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedLinksHandlerImpl implements DependentReferencedComponentHandler, ReferencedLinksConsumer {

    private final ReferencedLinkStorage storage;

    @Override
    public void maybeAsReference(Map<String, Link> links, Consumer<Map<String, Link>> setter) {
        setter.accept(links);
        storage.storeMaybeReferenceLinks(links);
    }

    @Override
    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return storage.getBuildDependencies();
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setLinks);
    }
}
