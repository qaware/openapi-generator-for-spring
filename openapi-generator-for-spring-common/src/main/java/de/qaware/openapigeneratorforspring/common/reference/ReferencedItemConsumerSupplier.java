package de.qaware.openapigeneratorforspring.common.reference;

@FunctionalInterface
public interface ReferencedItemConsumerSupplier {
    <T extends ReferencedItemConsumerForType<?>> T get(Class<T> referencedItemConsumerClazz);
}
