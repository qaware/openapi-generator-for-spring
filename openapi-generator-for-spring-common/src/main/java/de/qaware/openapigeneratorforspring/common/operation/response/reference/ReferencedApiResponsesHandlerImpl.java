package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedApiResponsesHandlerImpl implements ReferencedItemHandler<ApiResponses, ApiResponses>, ReferencedApiResponsesConsumer {
    private final ReferencedApiResponseStorage storage;

    @Override
    public void maybeAsReference(ApiResponses apiResponses, Consumer<ApiResponses> apiResponsesSetter) {
        apiResponsesSetter.accept(apiResponses);
        // exploit the fact that we've access to the full map of apiResponses
        // that means we can modify the reference, and don't need to call apiResponsesSetter again
        apiResponses.forEach((responseCode, apiResponse) ->
                storage.storeMaybeReference(responseCode, apiResponse,
                        referenceName -> apiResponses.put(responseCode, ApiResponse.builder().ref(referenceName.asReferenceString()).build())
                )
        );
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setResponses);
    }
}
