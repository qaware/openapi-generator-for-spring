package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationServersCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final ServerAnnotationMapper serverAnnotationMapper;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        setCollectionIfNotEmpty(serverAnnotationMapper.mapArray(operationAnnotation.servers()), operation::setServers);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
