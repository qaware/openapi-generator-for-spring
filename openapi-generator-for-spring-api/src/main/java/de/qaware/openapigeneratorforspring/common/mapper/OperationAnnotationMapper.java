package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.operation.Operation;

public interface OperationAnnotationMapper {
    Operation buildFromAnnotation(io.swagger.v3.oas.annotations.Operation operationAnnotation, MapperContext mapperContext);

    void applyFromAnnotation(Operation operation, io.swagger.v3.oas.annotations.Operation operationAnnotation, MapperContext mapperContext);
}
