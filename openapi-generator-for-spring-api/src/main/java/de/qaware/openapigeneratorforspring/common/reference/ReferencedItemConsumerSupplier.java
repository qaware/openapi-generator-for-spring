package de.qaware.openapigeneratorforspring.common.reference;


import javax.annotation.Nullable;

public interface ReferencedItemConsumerSupplier {
    <T extends ReferencedItemConsumer> T get(Class<T> referencedItemConsumerClazz);

    ReferencedItemConsumerSupplier withOwner(@Nullable Object owner);
}
