package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.requestbody.ReferencedRequestBodyConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultRequestBodyOperationCustomizer implements OperationCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    private final RequestBodyAnnotationMapper requestBodyAnnotationMapper;
    private final SchemaResolver schemaResolver;
    private final List<HandlerMethod.RequestBodyMapper> handlerMethodRequestBodyMappers;

    @Override
    public void customize(Operation operation, @Nullable io.swagger.v3.oas.annotations.Operation operationAnnotation, OperationBuilderContext operationBuilderContext) {
        setIfNotEmpty(
                applyFromMethod(operation.getRequestBody(), operationBuilderContext),
                requestBody -> operationBuilderContext.getMapperContext().getReferencedItemConsumer(ReferencedRequestBodyConsumer.class)
                        .maybeAsReference(requestBody, operation::setRequestBody)
        );
    }

    private RequestBody applyFromMethod(@Nullable RequestBody existingRequestBody, OperationBuilderContext operationBuilderContext) {
        // TODO use the request body parameter as a suggested identifier for possible referencing?
        return firstNonNull(handlerMethodRequestBodyMappers, mapper -> mapper.map(operationBuilderContext.getOperationInfo().getHandlerMethod()))
                .map(requestBodies -> buildRequestBody(requestBodies, existingRequestBody, operationBuilderContext))
                .orElseGet(RequestBody::new);
    }

    private RequestBody buildRequestBody(List<HandlerMethod.RequestBody> handlerMethodRequestBodies,
                                         @Nullable RequestBody existingRequestBody, OperationBuilderContext operationBuilderContext) {
        RequestBody requestBody = buildRequestBodyFromSwaggerAnnotations(existingRequestBody, handlerMethodRequestBodies, operationBuilderContext.getMapperContext());
        handlerMethodRequestBodies.forEach(handlerMethodRequestBodyParameter -> {
            for (String contentType : handlerMethodRequestBodyParameter.getConsumesContentTypes()) {
                MediaType mediaType = addMediaTypeIfNotPresent(contentType, requestBody);
                if (mediaType.getSchema() == null) {
                    handlerMethodRequestBodyParameter.getType().ifPresent(parameterType -> schemaResolver.resolveFromType(
                            parameterType.getType(),
                            handlerMethodRequestBodyParameter.getAnnotationsSupplier().andThen(parameterType.getAnnotationsSupplier()),
                            operationBuilderContext.getReferencedItemConsumer(ReferencedSchemaConsumer.class),
                            mediaType::setSchema
                    ));
                }
            }
            handlerMethodRequestBodyParameter.customize(requestBody);
        });
        return requestBody;
    }

    private RequestBody buildRequestBodyFromSwaggerAnnotations(@Nullable RequestBody existingRequestBody, List<HandlerMethod.RequestBody> methodParameters, MapperContext mapperContext) {
        RequestBody requestBody = Optional.ofNullable(existingRequestBody).orElseGet(RequestBody::new);
        methodParameters.stream()
                .flatMap(methodParameter -> methodParameter.getAnnotationsSupplier().findAnnotations(io.swagger.v3.oas.annotations.parameters.RequestBody.class))
                .forEach(swaggerRequestBodyAnnotation -> requestBodyAnnotationMapper.applyFromAnnotation(requestBody, swaggerRequestBodyAnnotation, mapperContext));
        return requestBody;
    }

    private static MediaType addMediaTypeIfNotPresent(String contentType, RequestBody requestBody) {
        if (requestBody.getContent() == null) {
            Content content = new Content();
            requestBody.setContent(content);
        }
        return requestBody.getContent().computeIfAbsent(contentType, ignored -> new MediaType());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
