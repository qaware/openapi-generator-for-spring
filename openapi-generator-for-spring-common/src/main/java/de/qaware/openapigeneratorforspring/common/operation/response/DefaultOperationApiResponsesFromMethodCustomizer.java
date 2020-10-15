package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
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
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();

        ApiResponse defaultApiResponse = getAndReplaceDefaultApiResponse(apiResponses, apiResponseCodeMapper.getFromMethod(method));

        if (Void.TYPE.equals(method.getReturnType()) || Void.class.equals(method.getReturnType())) {
            return;
        }

        Content content = getOrCreateEmptyContent(defaultApiResponse);

        AnnotationsSupplier annotationsSupplierFromMethodWithDeclaringClass = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method);
        List<String> producesContentType = getProducesContentType(annotationsSupplierFromMethodWithDeclaringClass);

        for (String contentType : producesContentType) {
            MediaType mediaType = content.computeIfAbsent(contentType, ignored -> new MediaType());
            // just using resolveFromClass here with method.getReturnType() does not work for generic return types
            // TODO check if supplying annotations from type, method and method's declaring class isn't too much searching
            AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(method.getReturnType())
                    .andThen(annotationsSupplierFromMethodWithDeclaringClass);
            schemaResolver.resolveFromType(method.getGenericReturnType(), annotationsSupplier,
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
            if (DEFAULT_ANNOTATION_RESPONSE_CODE.equals(responseCode)) {
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

    private static List<String> getProducesContentType(AnnotationsSupplier annotationsSupplierFromMethodWithDeclaringClass) {
        // TODO check if that logic here correctly mimics the way Spring is treating the "produces" property
        return annotationsSupplierFromMethodWithDeclaringClass
                .findAnnotations(RequestMapping.class)
                .filter(requestMappingAnnotation -> !StringUtils.isAllBlank(requestMappingAnnotation.produces()))
                .findFirst()
                .map(requestMappingAnnotation -> Arrays.asList(requestMappingAnnotation.produces()))
                // fallback to "all value" if nothing has been specified
                .orElse(Collections.singletonList(org.springframework.http.MediaType.ALL_VALUE));
    }
}
