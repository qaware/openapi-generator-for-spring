package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.With;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperContextImpl implements MapperContext {
    private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;
    @Nullable
    @With
    private final MediaTypesProvider mediaTypesProvider;

    public static MapperContextImpl of(ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return new MapperContextImpl(referencedItemConsumerSupplier, null);
    }

    @Override
    public <T extends ReferencedItemConsumer> T getReferencedItemConsumer(Class<T> referencedItemConsumerClazz) {
        return referencedItemConsumerSupplier.get(referencedItemConsumerClazz);
    }

    @Override
    public Optional<Set<String>> findMediaTypes(Class<? extends HasContent> owningType) {
        return Optional.ofNullable(mediaTypesProvider).map(provider -> provider.getMediaTypes(owningType));
    }

    @Override
    public MapperContext withReferencedItemOwner(@Nullable Object owner) {
        return new MapperContextImpl(referencedItemConsumerSupplier.withOwner(owner), mediaTypesProvider);
    }
}
