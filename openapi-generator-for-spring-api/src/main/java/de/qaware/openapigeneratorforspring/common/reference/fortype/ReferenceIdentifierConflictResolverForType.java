package de.qaware.openapigeneratorforspring.common.reference.fortype;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface ReferenceIdentifierConflictResolverForType<T> {
    default List<String> resolveConflict(List<T> itemsWithSameReferenceIdentifier, String originalIdentifier) {
        return IntStream.range(0, itemsWithSameReferenceIdentifier.size()).boxed()
                .map(i -> originalIdentifier + "_" + i)
                .collect(Collectors.toList());
    }
}
