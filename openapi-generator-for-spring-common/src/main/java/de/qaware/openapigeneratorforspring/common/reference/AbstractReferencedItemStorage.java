package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractReferencedItemStorage<T extends HasReference<T>, E extends AbstractReferencedItemStorage.ReferencableEntry<T>> {

    private static final String IDENTIFIER_SEPARATOR = "_";

    private final ReferenceType referenceType;
    private final ReferenceDeciderForType<T> referenceDecider;
    private final ReferenceIdentifierFactoryForType<T> referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForType<T> referenceIdentifierConflictResolver;
    private final Supplier<T> itemConstructor;
    private final Function<T, E> entryConstructor;

    private final List<E> entries = new ArrayList<>();

    protected E getEntryOrAddNew(T item) {
        return entries.stream()
                .filter(entry -> entry.matches(item))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one entry: " + a + " vs. " + b);
                })
                .orElseGet(() -> {
                    E newEntry = entryConstructor.apply(item);
                    entries.add(newEntry);
                    return newEntry;
                });
    }

    public Map<String, T> buildReferencedItems() {
        Map<String, T> referencedItems = new HashMap<>();
        entries.stream()
                .filter(entry -> entry.isReferenceRequired() || referenceDecider.turnIntoReference(entry.getItem(), entry.getReferenceSetters().count()))
                .collect(Collectors.groupingBy(this::createNonUniqueReferenceIdentifier))
                .forEach((referenceIdentifier, entriesGroupedByIdentifier) ->
                        buildUniqueReferenceIdentifiers(referenceIdentifier, entriesGroupedByIdentifier).forEach(
                                (uniqueIdentifier, entry) -> {
                                    String referencePath = referenceType.getReferencePrefix() + uniqueIdentifier;
                                    entry.getReferenceSetters().forEach(setter -> setter.accept(itemConstructor.get().createReference(referencePath)));
                                    referencedItems.put(uniqueIdentifier, entry.getItem());
                                })
                );
        return referencedItems;
    }

    private String createNonUniqueReferenceIdentifier(E entry) {
        return referenceIdentifierFactory.buildIdentifierComponents(entry.getItem(), entry.getSuggestedIdentifier()).stream()
                .map(Object::toString)
                .collect(Collectors.joining(IDENTIFIER_SEPARATOR));
    }

    private Map<String, ReferencableEntry<T>> buildUniqueReferenceIdentifiers(String referenceIdentifier, List<E> entriesGroupedByIdentifier) {
        if (entriesGroupedByIdentifier.size() == 1) {
            // special case: no conflicts need to be resolved as there's only one item for this identifier
            return Collections.singletonMap(referenceIdentifier, entriesGroupedByIdentifier.get(0));
        }

        List<T> itemsWithSameIdentifier = entriesGroupedByIdentifier.stream()
                .map(ReferencableEntry::getItem)
                .collect(Collectors.toList());

        List<String> uniqueIdentifiers = referenceIdentifierConflictResolver.resolveConflict(itemsWithSameIdentifier, referenceIdentifier);
        if (uniqueIdentifiers.size() != itemsWithSameIdentifier.size()) {
            throw new IllegalStateException(String.format("Conflict resolver %s did not return expected number %d but %d unique reference identifiers for items referenced by %s",
                    referenceIdentifierConflictResolver.getClass().getSimpleName(), itemsWithSameIdentifier.size(), uniqueIdentifiers.size(), referenceIdentifier));
        }

        // zip items into map
        return IntStream.range(0, entriesGroupedByIdentifier.size()).boxed()
                .collect(Collectors.toMap(
                        uniqueIdentifiers::get,
                        entriesGroupedByIdentifier::get,
                        (a, b) -> {
                            throw new IllegalStateException(String.format("Found non-unique reference identifier from conflict resolver %s: %s vs. %s",
                                    referenceIdentifierConflictResolver.getClass().getSimpleName(), a, b));
                        })
                );
    }

    public interface ReferencableEntry<T> {
        T getItem();

        boolean matches(T item);

        default boolean isReferenceRequired() {
            return false;
        }

        @Nullable
        String getSuggestedIdentifier();

        Stream<Consumer<T>> getReferenceSetters();

    }

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static abstract class AbstractReferencableEntry<T> implements ReferencableEntry<T> {
        @Getter
        private final T item;
        private final List<Consumer<T>> referenceSetters = new ArrayList<>();

        @Override
        public boolean matches(T item) {
            return this.item.equals(item);
        }

        public void addSetter(Consumer<T> setter) {
            referenceSetters.add(setter);
        }

        public Stream<Consumer<T>> getReferenceSetters() {
            return referenceSetters.stream();
        }
    }
}
