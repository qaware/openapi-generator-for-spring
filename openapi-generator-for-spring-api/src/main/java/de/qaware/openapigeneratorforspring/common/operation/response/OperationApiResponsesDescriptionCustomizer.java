package de.qaware.openapigeneratorforspring.common.operation.response;


/**
 * Intermediate interface for {@link
 * de.qaware.openapigeneratorforspring.model.response.ApiResponse
 * api response description} to support easier bean overriding.
 */
public interface OperationApiResponsesDescriptionCustomizer extends OperationApiResponsesCustomizer {
    int ORDER = OperationApiResponsesCustomizer.DEFAULT_ORDER + 1000; // make sure we run pretty late!

    @Override
    default int getOrder() {
        return ORDER;
    }
}
