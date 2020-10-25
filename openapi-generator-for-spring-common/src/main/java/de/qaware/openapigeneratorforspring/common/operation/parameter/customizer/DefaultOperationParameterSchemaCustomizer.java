package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
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
        ReferencedSchemaConsumer referencedSchemaConsumer = operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedSchemaConsumer.class);
        schemaResolver.resolveFromType(methodParameter.getType(), annotationsSupplier, referencedSchemaConsumer, parameter::setSchema);
    }
}
