package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.CallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferencedCallbacksConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationCallbackCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final CallbackAnnotationMapper callbackAnnotationMapper;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method);
        ReferencedItemConsumerSupplier referencedItemConsumerSupplier = operationBuilderContext.getReferencedItemConsumerSupplier();
        setMapIfNotEmpty(
                buildStringMapFromStream(
                        annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.callbacks.Callback.class),
                        io.swagger.v3.oas.annotations.callbacks.Callback::name,
                        callbackAnnotation -> callbackAnnotationMapper.map(callbackAnnotation, referencedItemConsumerSupplier)
                ),
                callbacks -> referencedItemConsumerSupplier.get(ReferencedCallbacksConsumer.class)
                        .maybeAsReference(callbacks, operation::setCallbacks)
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
