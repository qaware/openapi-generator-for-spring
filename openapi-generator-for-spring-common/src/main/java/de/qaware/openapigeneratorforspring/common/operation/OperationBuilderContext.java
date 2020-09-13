package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OperationBuilderContext {
    private final OperationInfo operationInfo;
    private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;
}
