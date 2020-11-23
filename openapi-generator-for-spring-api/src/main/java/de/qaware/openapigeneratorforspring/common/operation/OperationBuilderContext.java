package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.reference.HasReferencedItemConsumer;

public interface OperationBuilderContext extends HasReferencedItemConsumer {
    OperationInfo getOperationInfo();

    MapperContext getMapperContext();
}
