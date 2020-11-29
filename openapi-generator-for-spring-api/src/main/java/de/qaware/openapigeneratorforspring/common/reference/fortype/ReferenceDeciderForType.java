package de.qaware.openapigeneratorforspring.common.reference.fortype;

/**
 * Decides if the given to referenced item should become part of
 * the {@link de.qaware.openapigeneratorforspring.model.Components
 * Open Api model components}. Otherwise, it stays inlined.
 *
 * <p>Type-specific interfaces are typically overridden to
 * customize this decision (for example, one can control
 * if nothing shall be referenced for a given type).
 *
 * @param <T> type of the to be referenced item
 */
@FunctionalInterface
public interface ReferenceDeciderForType<T> {
    /**
     * Decide if item should become a reference and part of the
     * {@link de.qaware.openapigeneratorforspring.model.Components
     * Open Api model components}.
     *
     * <p>Default implementation returns {@code true}
     * if the {@code numberOfUsages} is larger than 1.
     *
     * @param item                item to be referenced
     * @param referenceIdentifier identifier for item
     * @param numberOfUsages      number of usages
     * @return true if item shall be referenced, false otherwise
     */
    boolean turnIntoReference(T item, String referenceIdentifier, long numberOfUsages);
}
