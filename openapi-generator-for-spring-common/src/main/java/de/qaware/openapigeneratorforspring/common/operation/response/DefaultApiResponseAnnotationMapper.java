package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultApiResponseAnnotationMapper implements ApiResponseAnnotationMapper {
    private final ApiResponseCodeMapper apiResponseCodeMapper;
    private final HeaderAnnotationMapper headerAnnotationMapper;
    private final ContentAnnotationMapper contentAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final LinkAnnotationMapper linkAnnotationMapper;

    @Override
    public ApiResponses buildApiResponsesFromAnnotations(List<ApiResponse> apiResponseAnnotations, OperationBuilderContext operationBuilderContext) {
        ApiResponses apiResponses = new ApiResponses();
        apiResponseAnnotations.forEach(annotation -> {
            String responseCode = apiResponseCodeMapper.map(annotation, operationBuilderContext);
            io.swagger.v3.oas.models.responses.ApiResponse apiResponse = apiResponses.computeIfAbsent(responseCode, ignored -> new io.swagger.v3.oas.models.responses.ApiResponse());
            setStringIfNotBlank(annotation.description(), apiResponse::setDescription);
            mergeWithExistingMap(apiResponse::getHeaders, apiResponse::setHeaders, headerAnnotationMapper.mapArray(annotation.headers(),
                    operationBuilderContext.getReferencedItemConsumerSupplier()));
            mergeWithExistingMap(apiResponse::getLinks, apiResponse::setLinks, linkAnnotationMapper.mapArray(annotation.links()));
            mergeWithExistingMap(apiResponse::getContent, apiResponse::setContent, contentAnnotationMapper.mapArray(annotation.content(),
                    operationBuilderContext.getReferencedItemConsumerSupplier()));
            mergeWithExistingMap(apiResponse::getExtensions, apiResponse::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
            setStringIfNotBlank(annotation.ref(), apiResponse::set$ref);
        });
        return apiResponses;
    }
}
