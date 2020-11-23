package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.CallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferencedCallbacksConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationCallbackCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final CallbackAnnotationMapper callbackAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();
        setMapIfNotEmpty(
                buildStringMapFromStream(
                        handlerMethod.findAnnotations(io.swagger.v3.oas.annotations.callbacks.Callback.class),
                        io.swagger.v3.oas.annotations.callbacks.Callback::name,
                        // note that mapping the callback should ignore media types relevant for the operation,
                        // so we map here with null handler method context
                        callbackAnnotation -> callbackAnnotationMapper.map(callbackAnnotation, operationBuilderContext.getMapperContext(null))
                ),
                callbacks -> operationBuilderContext.getReferencedItemConsumer(ReferencedCallbacksConsumer.class)
                        .maybeAsReference(callbacks, operation::setCallbacks)
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
