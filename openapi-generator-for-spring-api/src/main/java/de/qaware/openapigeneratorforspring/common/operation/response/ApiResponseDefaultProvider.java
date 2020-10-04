package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.model.response.ApiResponse;

public interface ApiResponseDefaultProvider {
    ApiResponse build(String responseCodeFromMethod);
}
