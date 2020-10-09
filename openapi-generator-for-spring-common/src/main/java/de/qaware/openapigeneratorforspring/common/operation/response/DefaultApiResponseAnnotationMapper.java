package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.reference.header.ReferencedHeadersConsumer;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultApiResponseAnnotationMapper implements ApiResponseAnnotationMapper {
    private final HeaderAnnotationMapper headerAnnotationMapper;
    private final ContentAnnotationMapper contentAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final LinkAnnotationMapper linkAnnotationMapper;

    @Override
    public ApiResponse buildFromAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        ApiResponse apiResponse = new ApiResponse();
        applyFromAnnotation(apiResponse, apiResponseAnnotation, referencedItemConsumerSupplier);
        return apiResponse;
    }

    @Override
    public void applyFromAnnotation(ApiResponse apiResponse, io.swagger.v3.oas.annotations.responses.ApiResponse annotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        setStringIfNotBlank(annotation.description(), apiResponse::setDescription);
        setCollectionIfNotEmpty(headerAnnotationMapper.mapArray(annotation.headers(), referencedItemConsumerSupplier),
                headers -> referencedItemConsumerSupplier.get(ReferencedHeadersConsumer.class).maybeAsReference(headers,
                        headersMap -> mergeWithExistingMap(apiResponse::getHeaders, apiResponse::setHeaders, headersMap)
                )
        );
        mergeWithExistingMap(apiResponse::getLinks, apiResponse::setLinks, linkAnnotationMapper.mapArray(annotation.links()));
        mergeWithExistingMap(apiResponse::getContent, apiResponse::setContent, contentAnnotationMapper.mapArray(annotation.content(), referencedItemConsumerSupplier));
        mergeWithExistingMap(apiResponse::getExtensions, apiResponse::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
        // TODO treat provided ref specially when present!
        setStringIfNotBlank(annotation.ref(), apiResponse::setRef);
    }
}
