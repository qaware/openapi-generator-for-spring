package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;

public interface ApiResponseAnnotationMapper {
    ApiResponse buildFromAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);

    void applyFromAnnotation(ApiResponse apiResponse, io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}
