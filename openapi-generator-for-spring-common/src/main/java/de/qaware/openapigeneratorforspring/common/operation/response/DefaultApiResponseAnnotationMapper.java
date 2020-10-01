package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferencedHeadersConsumer;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
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
        ReferencedItemConsumerSupplier referencedItemConsumerSupplier = operationBuilderContext.getReferencedItemConsumerSupplier();

        ApiResponses apiResponses = new ApiResponses();
        apiResponseAnnotations.forEach(annotation -> {
            String responseCode = apiResponseCodeMapper.map(annotation, operationBuilderContext);
            de.qaware.openapigeneratorforspring.model.response.ApiResponse apiResponse = apiResponses.computeIfAbsent(responseCode,
                    ignored -> de.qaware.openapigeneratorforspring.model.response.ApiResponse.builder().build()
            );
            setStringIfNotBlank(annotation.description(), apiResponse::setDescription);
            setCollectionIfNotEmpty(headerAnnotationMapper.mapArray(annotation.headers(), referencedItemConsumerSupplier),
                    headers -> referencedItemConsumerSupplier.get(ReferencedHeadersConsumer.class)
                            .maybeAsReference(headers,
                                    headersMap -> mergeWithExistingMap(apiResponse::getHeaders, apiResponse::setHeaders, headersMap)
                            )
            );
            mergeWithExistingMap(apiResponse::getLinks, apiResponse::setLinks, linkAnnotationMapper.mapArray(annotation.links()));
            mergeWithExistingMap(apiResponse::getContent, apiResponse::setContent, contentAnnotationMapper.mapArray(annotation.content(), referencedItemConsumerSupplier));
            mergeWithExistingMap(apiResponse::getExtensions, apiResponse::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
            setStringIfNotBlank(annotation.ref(), apiResponse::setRef);
        });
        return apiResponses;
    }
}
