package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.model.response.ApiResponse;

/**
 * Default {@link ApiResponse} provider. Can
 * be overridden to provide custom default.
 */
public interface ApiResponseDefaultProvider {
    /**
     * Build default api response for given response code.
     *
     * @param responseCode response code, typically inferred from {@link
     *                     de.qaware.openapigeneratorforspring.common.paths.HandlerMethod handler method}
     * @return default api response
     */
    ApiResponse build(String responseCode);
}
