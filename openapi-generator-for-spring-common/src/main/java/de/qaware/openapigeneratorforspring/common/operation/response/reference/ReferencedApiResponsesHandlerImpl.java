package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
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
                        referenceName -> apiResponses.put(responseCode, new ApiResponse().$ref(referenceName.asReferenceString()))
                )
        );
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setResponses);
    }
}
