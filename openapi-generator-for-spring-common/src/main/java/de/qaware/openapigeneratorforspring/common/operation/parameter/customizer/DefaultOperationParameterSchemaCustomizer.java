package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationParameterSchemaCustomizer implements OperationParameterCustomizer {
    private final SchemaResolver schemaResolver;

    @Override
    public void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter().ifPresent(handlerMethodParameter ->
                handlerMethodParameter.getType().ifPresent(parameterType -> {
                    // TODO handle explode setting of annotation?
                    ReferencedSchemaConsumer referencedSchemaConsumer = context.getMapperContext().getReferencedItemConsumer(ReferencedSchemaConsumer.class);
                    AnnotationsSupplier annotationsSupplier = handlerMethodParameter.getAnnotationsSupplier()
                            .andThen(parameterType.getAnnotationsSupplier());
                    schemaResolver.resolveFromType(parameterType.getType(), annotationsSupplier, referencedSchemaConsumer, parameter::setSchema);
                })
        );
    }
}
