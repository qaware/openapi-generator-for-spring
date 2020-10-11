package de.qaware.openapigeneratorforspring.common.reference.fortype;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public interface ReferencedItemConsumerForType<T, U> {
    void maybeAsReference(T item, Consumer<U> setter);

    default ReferencedItemConsumerForType<T, U> withOwner(@Nullable Object object) {
        return this;
    }
}
