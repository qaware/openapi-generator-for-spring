package de.qaware.openapigeneratorforspring.common.reference.fortype;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultReferenceIdentifierConflictResolverFactory {

    public <T, R extends ReferenceIdentifierConflictResolverForType<T>> R create(Function<ReferenceIdentifierConflictResolverForType<T>, R> mapper) {
        return mapper.apply((itemsWithSameReferenceIdentifier, originalIdentifier) ->
                IntStream.range(0, itemsWithSameReferenceIdentifier.size()).boxed()
                        .map(i -> originalIdentifier + "_" + i)
                        .collect(Collectors.toList())
        );
    }
}
