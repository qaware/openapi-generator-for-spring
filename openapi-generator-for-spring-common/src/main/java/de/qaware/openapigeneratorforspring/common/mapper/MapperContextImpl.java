package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.ifNullGet;
import static java.util.Collections.emptyMap;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperContextImpl implements MapperContext {
    private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;
    private final Map<Class<? extends HasContent>, Supplier<List<String>>> suggestedMediaTypesSuppliers;

    public static MapperContextImpl of(ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return new MapperContextImpl(referencedItemConsumerSupplier, emptyMap());
    }

    @Override
    public <T extends ReferencedItemConsumer> T getReferenceConsumer(Class<T> referencedItemConsumerClazz) {
        return referencedItemConsumerSupplier.get(referencedItemConsumerClazz);
    }

    @Override
    public List<String> getSuggestedMediaTypes(Class<? extends HasContent> owningType) {
        Supplier<List<String>> supplier = suggestedMediaTypesSuppliers.get(owningType);
        if (supplier == null) {
            throw new IllegalStateException("No media types available for " + owningType.getSimpleName());
        }
        return supplier.get();
    }

    @Override
    @CheckReturnValue
    public MapperContext withSuggestedMediaTypesSupplierFor(Class<? extends HasContent> owningType, Supplier<List<String>> suggestedMediaTypesSupplier) {
        Map<Class<? extends HasContent>, Supplier<List<String>>> suppliers = new HashMap<>(suggestedMediaTypesSuppliers);
        // prevent multiple calls to supplier by caching
        AtomicReference<List<String>> cache = new AtomicReference<>();
        suppliers.put(owningType,
                () -> cache.updateAndGet(current -> ifNullGet(current, suggestedMediaTypesSupplier))
        );
        return new MapperContextImpl(referencedItemConsumerSupplier, Collections.unmodifiableMap(suppliers));
    }

    @Override
    public MapperContext withReferenceOwner(@Nullable Object owner) {
        return new MapperContextImpl(referencedItemConsumerSupplier.withOwner(owner), suggestedMediaTypesSuppliers);
    }
}
