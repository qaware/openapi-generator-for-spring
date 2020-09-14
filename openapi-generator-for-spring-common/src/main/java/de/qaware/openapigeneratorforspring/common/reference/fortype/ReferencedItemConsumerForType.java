package de.qaware.openapigeneratorforspring.common.reference.fortype;

import java.util.function.Consumer;

public interface ReferencedItemConsumerForType<T> {
    void maybeAsReference(T item, Consumer<T> setter);
}
