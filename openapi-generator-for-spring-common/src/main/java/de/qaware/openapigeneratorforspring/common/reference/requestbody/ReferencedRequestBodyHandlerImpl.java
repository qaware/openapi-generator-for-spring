package de.qaware.openapigeneratorforspring.common.reference.requestbody;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedRequestBodyHandlerImpl implements
        ReferencedItemHandler<RequestBody, RequestBody>,
        ReferencedRequestBodyConsumer {

    private final ReferencedRequestBodyStorage storage;

    @Override
    public void maybeAsReference(RequestBody requestBody, Consumer<RequestBody> requestBodySetter) {
        requestBodySetter.accept(requestBody);
        storage.storeMaybeReference(requestBody, referenceName ->
                requestBodySetter.accept(RequestBody.builder().ref(referenceName.asReferenceString()).build())
        );
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setRequestBodies);
    }
}
