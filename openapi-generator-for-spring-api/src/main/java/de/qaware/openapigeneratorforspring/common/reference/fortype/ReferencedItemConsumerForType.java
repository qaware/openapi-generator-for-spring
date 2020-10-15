package de.qaware.openapigeneratorforspring.common.reference.fortype;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;

import java.util.function.Consumer;

public interface ReferencedItemConsumerForType<T, U> extends ReferencedItemConsumer {
    void maybeAsReference(T item, Consumer<U> setter);
}
