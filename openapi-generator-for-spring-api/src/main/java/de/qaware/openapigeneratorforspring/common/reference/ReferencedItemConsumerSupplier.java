package de.qaware.openapigeneratorforspring.common.reference;


import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;

import javax.annotation.Nullable;

public interface ReferencedItemConsumerSupplier {
    <T extends ReferencedItemConsumerForType<?, ?>> T get(Class<T> referencedItemConsumerClazz);

    ReferencedItemConsumerSupplier withOwner(@Nullable Object owner);
}
