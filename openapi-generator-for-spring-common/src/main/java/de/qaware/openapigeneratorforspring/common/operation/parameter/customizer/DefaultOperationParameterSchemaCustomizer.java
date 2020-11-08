package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationParameterSchemaCustomizer extends AbstractHandlerMethodParameterCustomizer {
    private final SchemaResolver schemaResolver;

    @Override
    public void customizeWithHandlerMethod(Parameter parameter, HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext) {
        handlerMethodParameter.getType().ifPresent(parameterType -> {
            // TODO handle explode setting of annotation?
            ReferencedSchemaConsumer referencedSchemaConsumer = operationBuilderContext.getMapperContext().getReferenceConsumer(ReferencedSchemaConsumer.class);
            schemaResolver.resolveFromType(parameterType, handlerMethodParameter.getAnnotationsSupplier()
                    .andThen(handlerMethodParameter.getAnnotationsSupplierForType()), referencedSchemaConsumer, parameter::setSchema);
        });
    }
}
