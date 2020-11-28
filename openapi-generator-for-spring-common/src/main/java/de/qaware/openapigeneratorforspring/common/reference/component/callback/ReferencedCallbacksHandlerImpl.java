package de.qaware.openapigeneratorforspring.common.reference.component.callback;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.handler.DependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.operation.Callback;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ReferencedCallbacksHandlerImpl implements DependentReferencedComponentHandler, ReferencedCallbacksConsumer {

    private final ReferencedCallbackStorage storage;

    @Override
    public void maybeAsReference(Map<String, Callback> callbacks, Consumer<Map<String, Callback>> setter) {
        setter.accept(callbacks);
        storage.storeMaybeReferenceCallbacks(callbacks);
    }

    @Override
    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return storage.getBuildDependencies();
    }

    @Override
    public void applyToComponents(Components components) {
        storage.buildReferencedItems(components::setCallbacks);
    }
}
