package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;

public interface OperationAnnotationMapper {
    Operation map(io.swagger.v3.oas.annotations.Operation operationAnnotation,
                  ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}