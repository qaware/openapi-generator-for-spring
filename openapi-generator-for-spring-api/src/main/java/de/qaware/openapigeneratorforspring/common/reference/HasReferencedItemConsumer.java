package de.qaware.openapigeneratorforspring.common.reference;

/**
 * Trait for providing a {@link ReferencedItemConsumer}
 * of specific sub-type. Typically implemented by using
 * a {@link ReferencedItemConsumerSupplier} instance.
 */
public interface HasReferencedItemConsumer {
    /**
     * Get the specific referenced item consumer.
     *
     * @param referencedItemConsumerClazz type of the consumer
     * @param <C>                         consumer type, extends {@link ReferencedItemConsumer}.
     * @return referenced item consumer
     */
    <C extends ReferencedItemConsumer> C getReferencedItemConsumer(Class<C> referencedItemConsumerClazz);
}
