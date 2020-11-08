package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

import java.util.Optional;

public interface OperationParameterCustomizerContext {
    OperationInfo getOperationInfo();

    MapperContext getMapperContext();

    Optional<HandlerMethod.Parameter> getHandlerMethodParameter();
}
