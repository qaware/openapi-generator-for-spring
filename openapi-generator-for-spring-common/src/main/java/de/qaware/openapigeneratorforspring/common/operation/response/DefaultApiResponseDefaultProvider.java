package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.model.response.ApiResponse;

public class DefaultApiResponseDefaultProvider implements ApiResponseDefaultProvider {

    @Override
    public ApiResponse build(String responseCode) {
        return new ApiResponse();
    }
}
