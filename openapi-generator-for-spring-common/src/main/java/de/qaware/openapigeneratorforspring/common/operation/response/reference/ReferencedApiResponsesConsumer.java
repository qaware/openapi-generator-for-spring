package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.function.Consumer;

public interface ReferencedApiResponsesConsumer {
    void maybeAsReferences(ApiResponses apiResponses, Consumer<ApiResponses> apiResponsesSetter);
}
