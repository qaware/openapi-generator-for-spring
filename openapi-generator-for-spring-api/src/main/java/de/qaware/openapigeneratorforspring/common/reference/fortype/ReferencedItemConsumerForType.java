package de.qaware.openapigeneratorforspring.common.reference.fortype;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;

import java.util.function.Consumer;

public interface ReferencedItemConsumerForType<T> extends ReferencedItemConsumer {
    void maybeAsReference(T item, Consumer<T> setter);
}
