package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.MapperContextImpl;
import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;

@RequiredArgsConstructor
public class OperationBuilder {

    private final OperationAnnotationMapper operationAnnotationMapper;
    private final List<OperationCustomizer> operationCustomizers;
    private final List<HandlerMethod.ResponseMapper> handlerMethodResponseMappers;
    private final List<HandlerMethod.RequestBodyMapper> handlerMethodRequestBodyMappers;

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

        Optional<List<HandlerMethod.Response>> responses = firstNonNull(handlerMethodResponseMappers, mapper -> mapper.map(handlerMethod));
        Optional<List<HandlerMethod.RequestBody>> requestBodies = firstNonNull(handlerMethodRequestBodyMappers, mapper -> mapper.map(handlerMethod));

        MapperContext mapperContext = MapperContextImpl.of(referencedItemConsumerSupplier)
                .withSuggestedMediaTypesSupplierFor(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody.class, () ->
                        requestBodies.map(Collection::stream)
                                .map(p -> p.map(HandlerMethod.RequestBody::getConsumesContentTypes).flatMap(Collection::stream).collect(Collectors.toList()))
                                .orElseThrow(() -> new IllegalStateException("No request body parameter found on handler method to supply media types"))
                )
                .withSuggestedMediaTypesSupplierFor(ApiResponse.class, () ->
                        responses.map(Collection::stream)
                                .map(p -> p.map(HandlerMethod.Response::getProducesContentTypes).flatMap(Collection::stream).collect(Collectors.toList()))
                                .orElseThrow(() -> new IllegalStateException("No return type found on handler method to supply media types"))
                );

        io.swagger.v3.oas.annotations.Operation operationAnnotation = handlerMethod.getAnnotationsSupplier()
                .findFirstAnnotation(io.swagger.v3.oas.annotations.Operation.class);

        Operation operation = Optional.ofNullable(operationAnnotation)
                .map(annotation -> operationAnnotationMapper.buildFromAnnotation(annotation, mapperContext))
                .orElseGet(Operation::new);

        OperationBuilderContext operationBuilderContext = OperationBuilderContextImpl.of(operationInfo, mapperContext);
        operationCustomizers.forEach(customizer -> customizer.customize(operation, operationAnnotation, operationBuilderContext));
        return operation;
    }
}
