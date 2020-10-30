package de.qaware.openapigeneratorforspring.common.reference.component.requestbody;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.handler.DependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedRequestBodyHandlerImpl implements DependentReferencedComponentHandler, ReferencedRequestBodyConsumer {

    private final ReferencedRequestBodyStorage storage;

    @Override
    public void maybeAsReference(RequestBody requestBody, Consumer<RequestBody> requestBodySetter) {
        requestBodySetter.accept(requestBody);
        storage.storeMaybeReference(requestBody, requestBodySetter);
    }

    @Override
    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return storage.getBuildDependencies();
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setRequestBodies);
    }
}
