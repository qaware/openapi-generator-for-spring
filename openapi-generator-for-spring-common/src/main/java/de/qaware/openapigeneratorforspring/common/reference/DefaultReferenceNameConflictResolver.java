package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.schema.Schema;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultReferenceNameConflictResolver implements ReferenceNameConflictResolver {

    @Override
    public List<ReferenceName> resolveConflict(List<Schema> schemasWithSameReferenceName, ReferenceName originalReferenceName) {
        return IntStream.range(0, schemasWithSameReferenceName.size()).boxed()
                .map(i -> originalReferenceName.withIdentifier(identifier -> identifier + "_" + i))
                .collect(Collectors.toList());
    }
}
