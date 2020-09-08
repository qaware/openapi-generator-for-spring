package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiCollectionUtils {

    public static <C extends Collection<T>, T> void setCollectionIfNotEmpty(C collection, Consumer<? super C> setter) {
        if (!collection.isEmpty()) {
            setter.accept(collection);
        }
    }

}
