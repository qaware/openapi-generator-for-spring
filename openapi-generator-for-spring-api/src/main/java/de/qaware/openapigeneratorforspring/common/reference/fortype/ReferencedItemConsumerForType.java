package de.qaware.openapigeneratorforspring.common.reference.fortype;

import java.util.function.Consumer;

public interface ReferencedItemConsumerForType<T, U> {
    void maybeAsReference(T item, Consumer<U> setter);
}
