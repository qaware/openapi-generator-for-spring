package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.MapperContextImpl;
import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;

@RequiredArgsConstructor
public class OperationBuilder {

    private final OperationAnnotationMapper operationAnnotationMapper;
    private final List<OperationCustomizer> operationCustomizers;
    private final List<HandlerMethod.ReturnTypeMapper> handlerMethodReturnTypeMappers;
    private final List<HandlerMethod.RequestBodyParameterMapper> handlerMethodRequestBodyParameterMappers;

    public Operation buildOperation(OperationInfo operationInfo, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        try {
            return buildOperationInternal(operationInfo, referencedItemConsumerSupplier);
        } catch (Exception e) {
            // wrap it into exception to help debugging failed open api builds
            throw new OperationBuilderException("Exception encountered while building operation with " + operationInfo, e);
        }
    }

    public Operation buildOperationInternal(OperationInfo operationInfo, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        HandlerMethod handlerMethod = operationInfo.getHandlerMethod();

        Optional<HandlerMethod.ReturnType> returnType = firstNonNull(handlerMethodReturnTypeMappers, mapper -> mapper.map(handlerMethod));
        Optional<HandlerMethod.RequestBodyParameter> requestBodyParameter = handlerMethod.getParameters().stream()
                .flatMap(methodParameter -> firstNonNull(handlerMethodRequestBodyParameterMappers, mapper -> mapper.map(methodParameter))
                        .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
                )
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one request body parameter on " + operationInfo);
                });

        MapperContext mapperContext = MapperContextImpl.of(referencedItemConsumerSupplier)
                .withSuggestedMediaTypesSupplierFor(RequestBody.class, () ->
                        requestBodyParameter.map(HandlerMethod.RequestBodyParameter::getConsumesContentTypes)
                                .orElseThrow(() -> new IllegalStateException("No request body parameter found on handler method to supply media types"))
                )
                .withSuggestedMediaTypesSupplierFor(ApiResponse.class, () ->
                        returnType.map(HandlerMethod.ReturnType::getProducesContentTypes)
                                .orElseThrow(() -> new IllegalStateException("No return type found on handler method to supply media types"))
                );

        io.swagger.v3.oas.annotations.Operation operationAnnotation = handlerMethod.getAnnotationsSupplier()
                .findFirstAnnotation(io.swagger.v3.oas.annotations.Operation.class);

        Operation operation = Optional.ofNullable(operationAnnotation)
                .map(annotation -> operationAnnotationMapper.map(annotation, mapperContext))
                .orElseGet(Operation::new);

        OperationBuilderContext operationBuilderContext = OperationBuilderContextImpl.of(operationInfo, mapperContext, returnType, requestBodyParameter);
        operationCustomizers.forEach(customizer -> customizer.customize(operation, operationAnnotation, operationBuilderContext));
        return operation;
    }
}
