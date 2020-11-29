package de.qaware.openapigeneratorforspring.common.reference.fortype;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

/**
 * Reference identifier builder. This identifier is used to point from various
 * places of the Open Api model into the corresponding definition in the
 * {@link de.qaware.openapigeneratorforspring.model.Components components}.
 *
 * <p>Identifier can be non-unique, see {@link
 * ReferenceIdentifierConflictResolverForType} for conflict resolution.
 *
 * <p>See also {@link ReferenceDeciderForType} for just controlling the reference decision
 *
 * @param <T> type of the to be referenced item
 */
@FunctionalInterface
public interface ReferenceIdentifierBuilderForType<T> {
    /**
     * @param item                item to be referenced
     * @param suggestedIdentifier suggested identifier (might be null)
     * @param numberOfSetters     number of setters consuming this item
     * @return identifier, or null/blank if item shall not be referenced
     */
    @Nullable
    String buildIdentifier(T item, @Nullable String suggestedIdentifier, int numberOfSetters);

    /**
     * Build identifiers for all given setters. Allows having
     * a shared identifier based on all the provided setters.
     *
     * @param item              to be referenced item
     * @param identifierSetters identifier setter, {@link IdentifierSetter#setValue} should be called
     */
    default void buildIdentifiers(T item, List<IdentifierSetter> identifierSetters) {
        // do not merge anything by default, and group by suggested identifier to avoid unnecessary calls to buildIdentifier
        identifierSetters.stream()
                .collect(groupingBy(IdentifierSetter::getSuggestedValue))
                .forEach((optionalSuggestedIdentifier, groupedIdentifierSetters) -> {
                    String identifier = buildIdentifier(item, optionalSuggestedIdentifier.orElse(null), groupedIdentifierSetters.size());
                    groupedIdentifierSetters.forEach(identifierSetter -> identifierSetter.setValue(identifier));
                });
    }

    /**
     * Interface to build a common identifier for
     * shared setters. See {@link #buildIdentifiers}.
     */
    interface IdentifierSetter {
        /**
         * Optional suggested value.
         *
         * @return suggested value for identifier
         */
        Optional<String> getSuggestedValue();

        /**
         * Flag if reference is required (identifier must be given then, otherwise building fails)
         *
         * @return true if reference required, false otherwise
         */
        boolean isReferenceRequired();

        /**
         * Consumer for the desired identifier value. If
         * this is never called, the item is not referenced.
         *
         * @param identifier value of the identifier
         */
        void setValue(@Nullable String identifier);
    }
}
