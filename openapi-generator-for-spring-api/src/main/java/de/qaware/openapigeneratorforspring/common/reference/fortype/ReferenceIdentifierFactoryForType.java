package de.qaware.openapigeneratorforspring.common.reference.fortype;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ReferenceIdentifierFactoryForType<T> {
    @Nullable
        // null means skip reference
    String buildIdentifier(T item, @Nullable String suggestedIdentifier, int numberOfSetters);
}
