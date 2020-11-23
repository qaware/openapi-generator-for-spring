package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;

public interface OperationBuilderContext {
    OperationInfo getOperationInfo();
    MapperContext getMapperContext();
}
