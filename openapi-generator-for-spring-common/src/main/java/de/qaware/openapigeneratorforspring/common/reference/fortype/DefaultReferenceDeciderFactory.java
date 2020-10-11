package de.qaware.openapigeneratorforspring.common.reference.fortype;

import java.util.function.Function;

public class DefaultReferenceDeciderFactory {
    public <T, R extends ReferenceDeciderForType<T>> R create(Function<ReferenceDeciderForType<T>, R> mapper) {
        return mapper.apply((item, numberOfUsages) -> numberOfUsages > 1);
    }
}
