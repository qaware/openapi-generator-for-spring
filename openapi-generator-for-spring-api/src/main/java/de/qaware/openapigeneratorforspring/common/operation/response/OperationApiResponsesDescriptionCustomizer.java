package de.qaware.openapigeneratorforspring.common.operation.response;


import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.muchLaterThan;

/**
 * Intermediate interface for {@link
 * de.qaware.openapigeneratorforspring.model.response.ApiResponse
 * api response description} to support easier bean overriding.
 */
public interface OperationApiResponsesDescriptionCustomizer extends OperationApiResponsesCustomizer {
    // make sure we run pretty late, as we don't want to interfere with other customizer possibly providing a better description
    int ORDER = muchLaterThan(OperationApiResponsesCustomizer.DEFAULT_ORDER);

    @Override
    default int getOrder() {
        return ORDER;
    }
}
