package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.CallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Callback;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import io.swagger.v3.oas.annotations.callbacks.Callbacks;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationCallbackCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final CallbackAnnotationMapper callbackAnnotationMapper;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromMethodWithDeclaringClass(operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod());
        Map<String, Callback> callbacks = buildStringMapFromStream(
                Stream.concat(
                        annotationsSupplier.findAnnotations(Callbacks.class).flatMap(annotation -> Stream.of(annotation.value())),
                        annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.callbacks.Callback.class)
                ),
                io.swagger.v3.oas.annotations.callbacks.Callback::name,
                callbackAnnotation -> callbackAnnotationMapper.map(callbackAnnotation,
                        operationBuilderContext.getReferencedItemConsumerSupplier(), operationBuilderContext.getTagsConsumer()
                )
        );
        setMapIfNotEmpty(callbacks, operation::setCallbacks);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
