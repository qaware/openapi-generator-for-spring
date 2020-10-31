package de.qaware.openapigeneratorforspring.common.reference.fortype;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@FunctionalInterface
public interface ReferenceIdentifierBuilderForType<T> {
    @Nullable
    String buildIdentifier(T item, @Nullable String suggestedIdentifier, int numberOfSetters);

    default void buildIdentifiers(T item, List<IdentifierSetter> identifierSetters) {
        // do not merge anything by default, and group by suggested identifier to avoid unnecessary calls to buildIdentifier
        identifierSetters.stream()
                .collect(groupingBy(IdentifierSetter::getSuggestedValue))
                .forEach((optionalSuggestedIdentifier, groupedIdentifierSetters) -> {
                    String identifier = buildIdentifier(item, optionalSuggestedIdentifier.orElse(null), groupedIdentifierSetters.size());
                    groupedIdentifierSetters.forEach(identifierSetter -> identifierSetter.setValue(identifier));
                });
    }

    interface IdentifierSetter {
        Optional<String> getSuggestedValue();

        boolean isReferenceRequired();

        void setValue(@Nullable String value);
    }
}
