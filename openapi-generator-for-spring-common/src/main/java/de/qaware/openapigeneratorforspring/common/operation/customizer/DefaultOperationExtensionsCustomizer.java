package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationExtensionsCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext, io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        setMapIfNotEmpty(
                extensionAnnotationMapper.mapArray(operationAnnotation.extensions()),
                operation::setExtensions
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
