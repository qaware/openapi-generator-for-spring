package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultMapperContext implements MapperContext {
    private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;
    @With
    @Getter
    private final List<String> suggestedMediaTypes;

    public static DefaultMapperContext of(ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return new DefaultMapperContext(referencedItemConsumerSupplier, new ArrayList<>());
    }

    @Override
    public <T extends ReferencedItemConsumer> T getReferenceConsumer(Class<T> referencedItemConsumerClazz) {
        return referencedItemConsumerSupplier.get(referencedItemConsumerClazz);
    }

    @Override
    public MapperContext withReferenceOwner(@Nullable Object owner) {
        return new DefaultMapperContext(referencedItemConsumerSupplier.withOwner(owner), suggestedMediaTypes);
    }
}
