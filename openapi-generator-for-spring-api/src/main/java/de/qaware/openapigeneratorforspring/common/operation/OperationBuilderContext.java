package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.HasReferencedItemConsumer;

import javax.annotation.Nullable;

public interface OperationBuilderContext extends HasReferencedItemConsumer {
    OperationInfo getOperationInfo();

    MapperContext getMapperContext(@Nullable HandlerMethod.Context context);
}
