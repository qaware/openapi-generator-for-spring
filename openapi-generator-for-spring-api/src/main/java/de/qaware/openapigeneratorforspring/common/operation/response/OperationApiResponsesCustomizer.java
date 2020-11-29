package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;

/**
 * Customizer for {@link ApiResponses}. Run AFTER the responses have been
 * built from {@link io.swagger.v3.oas.annotations.responses.ApiResponse
 * api response annotations}.
 */
@FunctionalInterface
public interface OperationApiResponsesCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize given responses via reference to {@link ApiResponses api responses map}.
     *
     * @param apiResponses            map of {@link de.qaware.openapigeneratorforspring.model.response.ApiResponse api responses}
     * @param operationBuilderContext context of operation building
     */
    void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext);
}
