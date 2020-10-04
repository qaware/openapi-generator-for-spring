package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import org.apache.commons.lang3.StringUtils;

public class DefaultOperationApiResponsesDescriptionCustomizer implements OperationApiResponsesDescriptionCustomizer {
    @Override
    public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
        apiResponses.forEach((responseCode, apiResponse) -> {
            // make sure a description is always given, as it's required by the OpenApi spec
            if (StringUtils.isBlank(apiResponse.getDescription())) {
                apiResponse.setDescription("Default response");
            }
        });
    }
}
