package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "of")
@Getter
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OperationBuilderContextImpl implements OperationBuilderContext {
    private final OperationInfo operationInfo;
    private final MapperContext mapperContext;
    private final Optional<List<HandlerMethod.ReturnType>> handlerMethodReturnTypes;
    private final Optional<List<HandlerMethod.RequestBodyParameter>> handlerMethodRequestBodyParameters;
}
