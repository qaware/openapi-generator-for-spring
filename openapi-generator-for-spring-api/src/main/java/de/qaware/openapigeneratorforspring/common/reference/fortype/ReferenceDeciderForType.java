package de.qaware.openapigeneratorforspring.common.reference.fortype;

public interface ReferenceDeciderForType<T> {
    default boolean turnIntoReference(T item, long numberOfUsages) {
        return numberOfUsages > 1;
    }
}
