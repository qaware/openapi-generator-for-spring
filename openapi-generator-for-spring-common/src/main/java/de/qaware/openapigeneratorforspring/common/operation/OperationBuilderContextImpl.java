package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class OperationBuilderContextImpl implements OperationBuilderContext {
    private final OperationInfo operationInfo;
    private final MapperContext mapperContext;
}
