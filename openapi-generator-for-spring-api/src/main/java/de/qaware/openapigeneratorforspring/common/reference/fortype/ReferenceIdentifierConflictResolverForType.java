package de.qaware.openapigeneratorforspring.common.reference.fortype;


import java.util.List;

@FunctionalInterface
public interface ReferenceIdentifierConflictResolverForType<T> {
    List<String> resolveConflict(List<T> itemsWithSameReferenceIdentifier, String originalIdentifier);
}
