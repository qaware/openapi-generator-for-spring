package de.qaware.openapigeneratorforspring.common.reference.component.header;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.handler.DependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.header.Header;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ReferencedHeadersHandlerImpl implements DependentReferencedComponentHandler, ReferencedHeadersConsumer {

    private final ReferencedHeaderStorage storage;

    @Override
    public void maybeAsReference(Map<String, Header> headers, Consumer<Map<String, Header>> headersSetter) {
        headersSetter.accept(headers);
        storage.maybeReferenceHeaders(headers);
    }

    @Override
    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return storage.getBuildDependencies();
    }

    @Override
    public void applyToComponents(Components components) {
        storage.buildReferencedItems(components::setHeaders);
    }
}
