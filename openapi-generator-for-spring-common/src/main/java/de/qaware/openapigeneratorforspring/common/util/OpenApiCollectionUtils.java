package de.qaware.openapigeneratorforspring.common.util;

import java.util.Collection;
import java.util.function.Consumer;

public class OpenApiCollectionUtils {

    private OpenApiCollectionUtils() {
        // static methods only
    }

    public static <C extends Collection<T>, T> void setCollectionIfNotEmpty(C collection, Consumer<? super C> setter) {
        if (!collection.isEmpty()) {
            setter.accept(collection);
        }
    }

}
