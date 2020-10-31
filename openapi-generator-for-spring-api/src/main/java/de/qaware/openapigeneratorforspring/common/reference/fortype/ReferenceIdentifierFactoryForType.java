package de.qaware.openapigeneratorforspring.common.reference.fortype;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

@FunctionalInterface
public interface ReferenceIdentifierFactoryForType<T> {
    @Nullable
    String buildIdentifier(T item, @Nullable String suggestedIdentifier, int numberOfSetters);

    default void mergeIdentifiers(T item, List<IdentifierSetter> identifierSetters) {
        // do not merge anything by default, and group by suggested identifier to avoid unnecessary calls to buildIdentifier
        identifierSetters.stream()
                // cannot use suggestedValue directly as null value is not allowed by groupingBy
                .collect(groupingBy(x -> x))
                .forEach((identifierSetter, groupedSetters) -> {
                    String identifier = buildIdentifier(item, identifierSetter.getSuggestedValue(), identifierSetters.size());
                    groupedSetters.forEach(consumer -> consumer.setValue(identifier));
                });
    }

    interface IdentifierSetter {
        @Nullable
        String getSuggestedValue();

        void setValue(@Nullable String value);

        boolean isReferenceRequired();
    }
}
