package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import org.springframework.core.Ordered;

/**
 * Customizer for {@link ApiResponses}. Run AFTER the responses have been
 * built from {@link io.swagger.v3.oas.annotations.responses.ApiResponse
 * api response annotations}.
 */
@FunctionalInterface
public interface OperationApiResponsesCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    /**
     * Customize given responses via reference to {@link ApiResponses api responses map}.
     *
     * @param apiResponses            map of {@link de.qaware.openapigeneratorforspring.model.response.ApiResponse api responses}
     * @param operationBuilderContext context of operation building
     */
    void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
