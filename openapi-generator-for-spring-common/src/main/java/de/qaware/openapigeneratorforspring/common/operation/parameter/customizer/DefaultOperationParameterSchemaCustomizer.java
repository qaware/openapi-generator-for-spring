package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;

@RequiredArgsConstructor
public class DefaultOperationParameterSchemaCustomizer extends AbstractHandlerMethodParameterCustomizer {
    private final SchemaResolver schemaResolver;
    private final List<HandlerMethod.ParameterTypeMapper> handlerMethodParameterTypeMappers;

    @Override
    public void customizeWithHandlerMethod(Parameter parameter, HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext) {
        firstNonNull(handlerMethodParameterTypeMappers, mapper -> mapper.map(handlerMethodParameter)).ifPresent(parameterType -> {
            // TODO handle explode setting of annotation?
            ReferencedSchemaConsumer referencedSchemaConsumer = operationBuilderContext.getMapperContext().getReferenceConsumer(ReferencedSchemaConsumer.class);
            schemaResolver.resolveFromType(parameterType, handlerMethodParameter.getAnnotationsSupplier()
                    .andThen(handlerMethodParameter.getAnnotationsSupplierForType()), referencedSchemaConsumer, parameter::setSchema);
        });
    }
}
