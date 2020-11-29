package de.qaware.openapigeneratorforspring.common.reference.fortype;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;

import java.util.function.Consumer;

/**
 * Interface for consuming to be referenced
 * items during Open Api model building.
 *
 * <p>Specific instances can be obtained via the {@link
 * de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier
 * supplier}.
 *
 * <p>By default, items are only "maybe" referenced. It is later decided by
 * {@link ReferenceDeciderForType} implementations if the item becomes actually
 * part of the {@link de.qaware.openapigeneratorforspring.model.Components
 * Open Api model components}
 *
 * @param <T> type of the to be referenced item
 * @see ReferenceIdentifierBuilderForType
 */
public interface ReferencedItemConsumerForType<T> extends ReferencedItemConsumer {
    /**
     * Register given item as maybe to be referenced.
     *
     * @param item   item
     * @param setter setter when this item belongs to.
     * @implNote Implementations are required to call the setter at
     * least once. It is called again if the given item is referenced.
     */
    void maybeAsReference(T item, Consumer<T> setter);
}
