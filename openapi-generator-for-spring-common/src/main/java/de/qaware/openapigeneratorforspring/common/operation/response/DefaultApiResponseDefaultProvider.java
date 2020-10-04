package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.model.response.ApiResponse;

public class DefaultApiResponseDefaultProvider implements ApiResponseDefaultProvider {

    @Override
    public ApiResponse build(String responseCodeFromMethod) {
        return new ApiResponse();
    }
}
