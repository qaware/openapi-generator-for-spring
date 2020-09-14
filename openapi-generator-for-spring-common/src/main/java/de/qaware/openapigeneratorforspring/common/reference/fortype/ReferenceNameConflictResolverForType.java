package de.qaware.openapigeneratorforspring.common.reference.fortype;


import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface ReferenceNameConflictResolverForType<T> {
    default List<ReferenceName> resolveConflict(List<T> itemsWithSameReferenceName, ReferenceName originalReferenceName) {
        return IntStream.range(0, itemsWithSameReferenceName.size()).boxed()
                .map(i -> originalReferenceName.withIdentifier(identifier -> identifier + "_" + i))
                .collect(Collectors.toList());
    }
}
