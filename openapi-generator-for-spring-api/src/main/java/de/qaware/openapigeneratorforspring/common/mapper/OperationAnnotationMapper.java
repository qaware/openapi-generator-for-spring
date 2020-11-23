package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.operation.Operation;

@FunctionalInterface
public interface OperationAnnotationMapper {
    Operation map(io.swagger.v3.oas.annotations.Operation operationAnnotation, MapperContext mapperContext);
}
