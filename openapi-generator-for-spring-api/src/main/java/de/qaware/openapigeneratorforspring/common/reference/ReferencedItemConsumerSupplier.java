package de.qaware.openapigeneratorforspring.common.reference;


import javax.annotation.Nullable;

/**
 * Supplier for referenced item consumers. Provided during open api
 * model building to collect to-be-referenced items of different types.
 */
public interface ReferencedItemConsumerSupplier {
    /**
     * Get a referenced item consumer of specific type.
     *
     * @param referencedItemConsumerClazz type of consumer
     * @param <C>                         consumer type, extends {@link ReferencedItemConsumer}.
     * @return reference item consumer, will fail if nothing is found
     */
    <C extends ReferencedItemConsumer> C get(Class<C> referencedItemConsumerClazz);

    /**
     * Set the owner for all following consumed items via {@link #get}.
     *
     * @param owner owner, or null if no owner
     * @return modified referenced item consumer supplier
     */
    ReferencedItemConsumerSupplier withOwner(@Nullable Object owner);
}
