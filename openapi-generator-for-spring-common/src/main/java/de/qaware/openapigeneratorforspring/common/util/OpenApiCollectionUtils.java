package de.qaware.openapigeneratorforspring.common.util;

import java.util.Collection;
import java.util.function.Consumer;

public class OpenApiCollectionUtils {

    private OpenApiCollectionUtils() {
        // static methods only
    }

    public static <C extends Collection<T>, T> void setCollectionIfNotEmpty(Consumer<? super C> setter, C collection) {
        if (!collection.isEmpty()) {
            setter.accept(collection);
        }
    }

}
