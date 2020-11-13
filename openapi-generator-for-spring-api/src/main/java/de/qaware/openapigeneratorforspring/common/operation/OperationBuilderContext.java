package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

import java.util.List;
import java.util.Optional;

public interface OperationBuilderContext {
    OperationInfo getOperationInfo();

    MapperContext getMapperContext();

    Optional<List<HandlerMethod.ReturnType>> getHandlerMethodReturnTypes();

    Optional<List<HandlerMethod.RequestBodyParameter>> getHandlerMethodRequestBodyParameters();
}
