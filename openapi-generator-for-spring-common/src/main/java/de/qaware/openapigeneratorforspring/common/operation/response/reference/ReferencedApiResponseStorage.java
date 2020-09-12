package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import io.swagger.v3.oas.models.responses.ApiResponse;

import java.util.Map;
import java.util.function.Consumer;

public interface ReferencedApiResponseStorage {
    void storeApiResponseMaybeReference(String responseCode, ApiResponse apiResponse, Consumer<ReferenceName> referenceNameConsumer);

    Map<String, ApiResponse> buildReferencedApiResponses();
}
