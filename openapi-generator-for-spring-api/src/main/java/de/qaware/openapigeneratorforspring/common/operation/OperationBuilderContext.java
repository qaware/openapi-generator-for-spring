package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

import java.util.Optional;

public interface OperationBuilderContext {
    OperationInfo getOperationInfo();

    MapperContext getMapperContext();

    Optional<HandlerMethod.ReturnType> getHandlerMethodReturnType();

    Optional<HandlerMethod.RequestBodyParameter> getHandlerMethodRequestBodyParameter();
}
