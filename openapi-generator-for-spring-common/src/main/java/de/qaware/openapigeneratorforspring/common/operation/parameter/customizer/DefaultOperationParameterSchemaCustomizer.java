package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodParameterTypeMapper;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class DefaultOperationParameterSchemaCustomizer implements OperationParameterCustomizer {
    private final SchemaResolver schemaResolver;
    private final HandlerMethodParameterTypeMapper handlerMethodParameterTypeMapper;

    @Override
    public void customize(Parameter parameter, HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext) {
        // TODO handle explode setting of annotation?
        Type parameterType = handlerMethodParameterTypeMapper.map(handlerMethodParameter);
        if (parameterType != null) {
            ReferencedSchemaConsumer referencedSchemaConsumer = operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedSchemaConsumer.class);
            schemaResolver.resolveFromType(parameterType, handlerMethodParameter.getAnnotationsSupplier()
                    .andThen(handlerMethodParameter.getAnnotationsSupplierForType()), referencedSchemaConsumer, parameter::setSchema);
        }
    }
}
