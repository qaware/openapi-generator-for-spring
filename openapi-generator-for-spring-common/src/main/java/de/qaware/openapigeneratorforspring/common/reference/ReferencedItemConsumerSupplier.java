package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;

@FunctionalInterface
public interface ReferencedItemConsumerSupplier {
    <T extends ReferencedItemConsumerForType<?, ?>> T get(Class<T> referencedItemConsumerClazz);
}
