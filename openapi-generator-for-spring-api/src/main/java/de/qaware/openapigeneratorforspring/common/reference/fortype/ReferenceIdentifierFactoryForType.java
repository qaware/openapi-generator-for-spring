package de.qaware.openapigeneratorforspring.common.reference.fortype;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ReferenceIdentifierFactoryForType<T> {
    String buildIdentifier(T item, @Nullable String suggestedIdentifier);
}
