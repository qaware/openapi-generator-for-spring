package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class OperationBuilderContextImpl implements OperationBuilderContext {
    private final OperationInfo operationInfo;
    private final MapperContext mapperContext;
    private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;

    @Override
    public <T extends ReferencedItemConsumer> T getReferencedItemConsumer(Class<T> referencedItemConsumerClazz) {
        return referencedItemConsumerSupplier.get(referencedItemConsumerClazz);
    }
}
