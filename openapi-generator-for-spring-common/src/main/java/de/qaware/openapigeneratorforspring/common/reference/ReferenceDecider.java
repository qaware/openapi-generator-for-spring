package de.qaware.openapigeneratorforspring.common.reference;

public interface ReferenceDecider<T> {
    default boolean turnIntoReference(T item, int numberOfUsages) {
        return numberOfUsages > 1;
    }
}
