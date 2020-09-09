package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationParameterSchemaCustomizer implements OperationParameterCustomizer {
    private final SchemaResolver schemaResolver;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Parameter parameter, java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier, OperationBuilderContext operationBuilderContext) {
        // TODO handle explode setting of annotation?
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(methodParameter)
                .andThen(annotationsSupplierFactory.createFromAnnotatedElement(methodParameter.getType()));
        schemaResolver.resolveFromType(methodParameter.getType(), annotationsSupplier, operationBuilderContext.getReferencedSchemaConsumer(),
                parameter::setSchema
        );
    }
}
