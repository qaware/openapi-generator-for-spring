package de.qaware.openapigeneratorforspring.common.reference.requestbody;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.RequestBody;
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
                requestBodySetter.accept(new RequestBody().$ref(referenceName.asReferenceString()))
        );
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setRequestBodies);
    }
}
