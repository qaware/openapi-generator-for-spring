package de.qaware.openapigeneratorforspring.common.reference.component.requestbody;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedRequestBodyHandlerImpl implements ReferencedComponentHandler, ReferencedRequestBodyConsumer {

    private final ReferencedRequestBodyStorage storage;

    @Override
    public void maybeAsReference(RequestBody requestBody, Consumer<RequestBody> requestBodySetter) {
        requestBodySetter.accept(requestBody);
        storage.storeMaybeReference(requestBody, requestBodySetter);
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setRequestBodies);
    }
}
