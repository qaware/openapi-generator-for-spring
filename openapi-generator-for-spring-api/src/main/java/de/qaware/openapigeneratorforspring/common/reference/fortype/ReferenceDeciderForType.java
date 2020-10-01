package de.qaware.openapigeneratorforspring.common.reference.fortype;

public interface ReferenceDeciderForType<T> {
    default boolean turnIntoReference(T item, int numberOfUsages) {
        return numberOfUsages > 1;
    }
}
