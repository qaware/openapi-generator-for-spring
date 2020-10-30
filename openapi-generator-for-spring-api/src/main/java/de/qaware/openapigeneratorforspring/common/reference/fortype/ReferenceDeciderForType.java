package de.qaware.openapigeneratorforspring.common.reference.fortype;

@FunctionalInterface
public interface ReferenceDeciderForType<T> {
    boolean turnIntoReference(T item, String referenceIdentifier, long numberOfUsages);
}
