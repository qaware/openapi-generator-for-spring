package de.qaware.openapigeneratorforspring.common.reference.fortype;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@FunctionalInterface
public interface ReferenceIdentifierFactoryForType<T> {
    @Nullable
    String buildIdentifier(T item, @Nullable String suggestedIdentifier, int numberOfSetters);

    default void mergeIdentifiers(T item, List<IdentifierSetter> identifierSetters) {
        // do not merge anything by default, and group by suggested identifier to avoid unnecessary calls to buildIdentifier
        identifierSetters.stream()
                // cannot use suggestedValue directly as null value is not allowed by groupingBy
                .collect(groupingBy(identifierSetter -> Optional.ofNullable(identifierSetter.getSuggestedValue())))
                .forEach((optionalSuggestedIdentifier, groupedIdentifierSetters) -> {
                    String identifier = buildIdentifier(item, optionalSuggestedIdentifier.orElse(null), groupedIdentifierSetters.size());
                    groupedIdentifierSetters.forEach(identifierSetter -> identifierSetter.setValue(identifier));
                });
    }

    interface IdentifierSetter {
        @Nullable
        String getSuggestedValue();

        boolean isReferenceRequired();

        void setValue(@Nullable String value);
    }
}
