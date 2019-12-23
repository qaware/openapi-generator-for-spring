package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationExtensionsCustomizer implements OperationCustomizer {

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext, io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        setMapIfNotEmpty(
                operation::setExtensions,
                extensionAnnotationMapper.mapArray(operationAnnotation.extensions())
        );
    }
}
