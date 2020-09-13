package de.qaware.openapigeneratorforspring.common.reference;

import java.util.function.Consumer;

public interface ReferencedItemConsumerForType<T> {
    void maybeAsReference(T item, Consumer<T> setter);
}
