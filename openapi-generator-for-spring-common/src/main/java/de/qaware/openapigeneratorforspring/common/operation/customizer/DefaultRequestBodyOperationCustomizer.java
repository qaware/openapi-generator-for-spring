package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
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
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultRequestBodyOperationCustomizer implements OperationCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    private final RequestBodyAnnotationMapper requestBodyAnnotationMapper;
    private final List<HandlerMethod.ParameterTypeMapper> handlerMethodParameterTypeMappers;
    private final List<HandlerMethod.RequestBodyParameterMapper> handlerMethodRequestBodyParameterMappers;
    private final SchemaResolver schemaResolver;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        setIfNotEmpty(
                applyFromMethod(operation.getRequestBody(), operationBuilderContext),
                requestBody -> operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedRequestBodyConsumer.class)
                        .maybeAsReference(requestBody, operation::setRequestBody)
        );
    }

    private RequestBody applyFromMethod(@Nullable RequestBody existingRequestBody, OperationBuilderContext operationBuilderContext) {
        OperationInfo operationInfo = operationBuilderContext.getOperationInfo();
        ReferencedItemConsumerSupplier referencedItemConsumerSupplier = operationBuilderContext.getReferencedItemConsumerSupplier();
        return operationInfo.getHandlerMethod().getParameters().stream()
                .flatMap(methodParameter -> firstNonNull(handlerMethodRequestBodyParameterMappers, mapper -> mapper.map(methodParameter))
                        .map(handlerMethodRequestBody -> buildRequestBody(handlerMethodRequestBody, methodParameter, existingRequestBody, referencedItemConsumerSupplier))
                        .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
                )
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one @RequestBody annotation on " + operationInfo);
                })
                .orElseGet(RequestBody::new);
    }

    private RequestBody buildRequestBody(HandlerMethod.RequestBodyParameter handlerMethodRequestBody, HandlerMethod.Parameter methodParameter, @Nullable RequestBody existingRequestBody, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        RequestBody requestBody = getRequestBodyFromAnnotation(existingRequestBody, methodParameter, referencedItemConsumerSupplier);
        handlerMethodRequestBody.customize(requestBody);
        for (String contentType : handlerMethodRequestBody.getConsumesContentTypes()) {
            MediaType mediaType = getMediaType(contentType, requestBody);
            if (mediaType.getSchema() == null) {
                firstNonNull(handlerMethodParameterTypeMappers, mapper -> mapper.map(methodParameter))
                        .ifPresent(parameterType -> schemaResolver.resolveFromType(
                                parameterType,
                                methodParameter.getAnnotationsSupplier().andThen(methodParameter.getAnnotationsSupplierForType()),
                                referencedItemConsumerSupplier.get(ReferencedSchemaConsumer.class),
                                mediaType::setSchema
                        ));
            }
        }
        return requestBody;
    }

    private RequestBody getRequestBodyFromAnnotation(@Nullable RequestBody existingRequestBody, HandlerMethod.Parameter methodParameter, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return Optional.ofNullable(methodParameter.getAnnotationsSupplier().findFirstAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class))
                .map(swaggerRequestBodyAnnotation -> {
                    if (existingRequestBody != null) {
                        requestBodyAnnotationMapper.applyFromAnnotation(existingRequestBody, swaggerRequestBodyAnnotation, referencedItemConsumerSupplier);
                        return existingRequestBody;
                    }
                    return requestBodyAnnotationMapper.buildFromAnnotation(swaggerRequestBodyAnnotation, referencedItemConsumerSupplier);
                })
                .orElseGet(() -> existingRequestBody != null ? existingRequestBody : new RequestBody());
    }

    private static MediaType getMediaType(String contentType, RequestBody requestBody) {
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
