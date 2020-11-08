package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.CallbackAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferencedCallbacksConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationCallbackCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final CallbackAnnotationMapper callbackAnnotationMapper;

    @Override
    public void customize(Operation operation, @Nullable io.swagger.v3.oas.annotations.Operation operationAnnotation, OperationBuilderContext operationBuilderContext) {
        AnnotationsSupplier annotationsSupplier = operationBuilderContext.getOperationInfo().getHandlerMethod().getAnnotationsSupplier();
        MapperContext mapperContext = operationBuilderContext.getMapperContext();
        setMapIfNotEmpty(
                buildStringMapFromStream(
                        annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.callbacks.Callback.class),
                        io.swagger.v3.oas.annotations.callbacks.Callback::name,
                        callbackAnnotation -> callbackAnnotationMapper.map(callbackAnnotation, mapperContext)
                ),
                callbacks -> mapperContext.getReferenceConsumer(ReferencedCallbacksConsumer.class)
                        .maybeAsReference(callbacks, operation::setCallbacks)
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
