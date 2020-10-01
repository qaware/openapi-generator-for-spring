package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;

public interface OperationApiResponsesCustomizer {
    void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext);
}
