package de.qaware.openapigeneratorforspring.common.reference.fortype;


import java.util.List;

/**
 * Conflict resolver for non-unique identifiers returned by {@link ReferenceIdentifierBuilderForType}.
 *
 * @param <T> type of the to be referenced item
 */
@FunctionalInterface
public interface ReferenceIdentifierConflictResolverForType<T> {
    /**
     * From the list of given items with same identifier, builds
     * a list of same length containing unique identifiers.
     *
     * <p>Default implementation just adds indexes as suffix to each item.
     *
     * @param itemsWithSameReferenceIdentifier conflicting items with same identifier
     * @param originalIdentifier               original shared identifier for all items
     * @return list of unique identifiers
     */
    List<String> resolveConflict(List<T> itemsWithSameReferenceIdentifier, String originalIdentifier);
}
