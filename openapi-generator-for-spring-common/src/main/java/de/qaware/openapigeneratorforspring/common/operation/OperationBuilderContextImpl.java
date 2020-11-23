package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "of")
public class OperationBuilderContextImpl implements OperationBuilderContext {
    @Getter
    private final OperationInfo operationInfo;
    private final Function<HandlerMethod.Context, MapperContext> mapperContextSupplier;
    private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;

    @Override
    public <T extends ReferencedItemConsumer> T getReferencedItemConsumer(Class<T> referencedItemConsumerClazz) {
        return referencedItemConsumerSupplier.get(referencedItemConsumerClazz);
    }

    @Override
    public MapperContext getMapperContext(@Nullable HandlerMethod.Context context) {
        return mapperContextSupplier.apply(context);
    }
}
