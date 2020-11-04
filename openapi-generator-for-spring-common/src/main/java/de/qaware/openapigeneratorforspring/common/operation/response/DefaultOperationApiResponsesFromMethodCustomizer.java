package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodReturnTypeMapper;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static de.qaware.openapigeneratorforspring.common.operation.response.DefaultApiResponseCodeMapper.DEFAULT_ANNOTATION_RESPONSE_CODE;

@RequiredArgsConstructor
public class DefaultOperationApiResponsesFromMethodCustomizer implements OperationApiResponsesFromMethodCustomizer {

    private final ApiResponseCodeMapper apiResponseCodeMapper;
    private final ApiResponseDefaultProvider apiResponseDefaultProvider;
    private final SchemaResolver schemaResolver;
    private final HandlerMethodReturnTypeMapper handlerMethodReturnTypeMapper;

    @Override
    public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();

        ApiResponse defaultApiResponse = getAndReplaceDefaultApiResponse(apiResponses, apiResponseCodeMapper.map(handlerMethod));

        HandlerMethod.ReturnType handlerMethodReturnType = handlerMethodReturnTypeMapper.map(handlerMethod);
        if (handlerMethodReturnType == null) {
            return;
        }

        Content content = getOrCreateEmptyContent(defaultApiResponse);
        List<String> producesContentType = getProducesContentType(handlerMethod);
        for (String contentType : producesContentType) {
            MediaType mediaType = content.computeIfAbsent(contentType, ignored -> new MediaType());
            // just using resolveFromClass here with method.getReturnType() does not work for generic return types
            // TODO check if supplying annotations from return type, method and method's declaring class isn't too much searching
            AnnotationsSupplier annotationsSupplier = handlerMethodReturnType.getAnnotationsSupplier()
                    .andThen(handlerMethod.getAnnotationsSupplier());
            schemaResolver.resolveFromType(handlerMethodReturnType.getType(), annotationsSupplier,
                    operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedSchemaConsumer.class),
                    mediaType::setSchema
            );
        }

    }

    private ApiResponse getAndReplaceDefaultApiResponse(ApiResponses apiResponses, String responseCodeFromMethod) {
        // we want to keep the place at which the default API response is inserted,
        // but we want to change the "default" response to the response code of the method
        AtomicReference<ApiResponse> defaultApiResponseHolder = new AtomicReference<>();
        ApiResponses modified = new ApiResponses();
        apiResponses.forEach((responseCode, apiResponse) -> {
            if (DEFAULT_ANNOTATION_RESPONSE_CODE.equals(responseCode) || responseCodeFromMethod.equals(responseCode)) {
                if (defaultApiResponseHolder.get() != null) {
                    // we cannot meaningfully merge those two API responses, so bail out
                    throw new IllegalStateException("Found default API response object for key " + DEFAULT_ANNOTATION_RESPONSE_CODE
                            + " and method code " + responseCodeFromMethod + ". Only one variant is supported for default response.");
                }
                defaultApiResponseHolder.set(apiResponse);
                modified.put(responseCodeFromMethod, apiResponse);
            } else {
                modified.put(responseCode, apiResponse);
            }
        });

        // update via given reference to original map
        apiResponses.clear();
        apiResponses.putAll(modified);

        // fallback to provider if default isn't present
        if (defaultApiResponseHolder.get() == null) {
            ApiResponse defaultApiResponse = apiResponseDefaultProvider.build(responseCodeFromMethod);
            apiResponses.put(responseCodeFromMethod, defaultApiResponse);
            return defaultApiResponse;
        }
        return defaultApiResponseHolder.get();
    }

    private static Content getOrCreateEmptyContent(ApiResponse apiResponse) {
        if (apiResponse.getContent() != null) {
            return apiResponse.getContent();
        }
        Content content = new Content();
        apiResponse.setContent(content);
        return content;
    }

    private static List<String> getProducesContentType(HandlerMethod handlerMethod) {
        // TODO check if that logic here correctly mimics the way Spring is treating the "produces" property
        return handlerMethod.getAnnotationsSupplier()
                .findAnnotations(RequestMapping.class)
                .filter(requestMappingAnnotation -> !StringUtils.isAllBlank(requestMappingAnnotation.produces()))
                .findFirst()
                .map(requestMappingAnnotation -> Arrays.asList(requestMappingAnnotation.produces()))
                // fallback to "all value" if nothing has been specified
                .orElse(Collections.singletonList(org.springframework.http.MediaType.ALL_VALUE));
    }
}
