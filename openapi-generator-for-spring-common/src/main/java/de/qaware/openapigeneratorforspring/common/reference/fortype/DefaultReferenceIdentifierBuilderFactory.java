package de.qaware.openapigeneratorforspring.common.reference.fortype;

import java.util.function.Function;

public class DefaultReferenceIdentifierBuilderFactory {
    public <T, R extends ReferenceIdentifierBuilderForType<T>> R create(Function<ReferenceIdentifierBuilderForType<T>, R> mapper) {
        return mapper.apply((item, suggestedIdentifier, numberOfUsages) -> suggestedIdentifier);
    }
}
