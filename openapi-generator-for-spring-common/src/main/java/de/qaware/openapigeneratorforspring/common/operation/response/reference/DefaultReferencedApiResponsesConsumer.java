package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class DefaultReferencedApiResponsesConsumer implements ReferencedApiResponsesConsumer {
    private final ReferencedApiResponseStorage referencedApiResponseStorage;

    @Override
    public void maybeAsReferences(ApiResponses apiResponses, Consumer<ApiResponses> apiResponsesSetter) {
        apiResponsesSetter.accept(apiResponses);
        // exploit the fact that we've access to the full map of apiResponses
        // that means we can modify the reference, and don't need to call apiResponsesSetter again
        apiResponses.forEach((responseCode, apiResponse) ->
                referencedApiResponseStorage.storeApiResponseMaybeReference(responseCode, apiResponse,
                        referenceName -> apiResponses.put(responseCode, new ApiResponse().$ref(referenceName.asReferenceString()))
                )
        );
    }
}
