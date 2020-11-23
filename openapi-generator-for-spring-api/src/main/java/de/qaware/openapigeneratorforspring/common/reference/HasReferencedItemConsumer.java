package de.qaware.openapigeneratorforspring.common.reference;

public interface HasReferencedItemConsumer {
    <T extends ReferencedItemConsumer> T getReferencedItemConsumer(Class<T> referencedItemConsumerClazz);
}
