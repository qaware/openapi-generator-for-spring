package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;

@RequiredArgsConstructor
public class DefaultOperationApiResponsesFromMethodCustomizer implements OperationApiResponsesFromMethodCustomizer {

    // TODO turn this into a supplier bean!
    static final String DEFAULT_ANNOTATION_RESPONSE_CODE = MergedAnnotation.of(io.swagger.v3.oas.annotations.responses.ApiResponse.class)
            .getDefaultValue("responseCode", String.class)
            .orElseThrow(() -> new IllegalStateException("Cannot infer default response code from ApiResponse annotation"));

    private final List<HandlerMethod.ApiResponseCodeMapper> apiResponseCodeMappers;
    private final ApiResponseDefaultProvider apiResponseDefaultProvider;
    private final SchemaResolver schemaResolver;

    @Override
    public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();
        ApiResponse defaultApiResponse = getAndReplaceDefaultApiResponse(apiResponses,
                firstNonNull(apiResponseCodeMappers, mapper -> mapper.map(handlerMethod))
                        .orElseThrow(() -> new IllegalStateException("Cannot find api response code for " + handlerMethod))
        );
        operationBuilderContext.getHandlerMethodReturnType().ifPresent(handlerMethodReturnType -> {
            Content content = getOrCreateEmptyContent(defaultApiResponse);
            addMediaTypesToContent(operationBuilderContext, handlerMethod, handlerMethodReturnType, content);
        });
    }

    private void addMediaTypesToContent(OperationBuilderContext operationBuilderContext, HandlerMethod handlerMethod, HandlerMethod.ReturnType handlerMethodReturnType, Content content) {
        List<String> producesContentType = handlerMethodReturnType.getProducesContentTypes();
        for (String contentType : producesContentType) {
            MediaType mediaType = content.computeIfAbsent(contentType, ignored -> new MediaType());
            if (mediaType.getSchema() != null) {
                // we might have already set some media type, only amend this if the schema is not present
                // this gives annotations a higher preference than the schema inferred from the method return type
                continue;
            }
            // TODO check if supplying annotations from return type, method and method's declaring class isn't too much searching
            AnnotationsSupplier annotationsSupplier = handlerMethodReturnType.getAnnotationsSupplier()
                    .andThen(handlerMethod.getAnnotationsSupplier());
            schemaResolver.resolveFromType(handlerMethodReturnType.getType(), annotationsSupplier,
                    operationBuilderContext.getMapperContext().getReferenceConsumer(ReferencedSchemaConsumer.class),
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
}
