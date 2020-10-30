package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.common.util.OpenApiProxyUtils;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractReferencedItemStorage<
        T extends HasReference<T>,
        E extends AbstractReferencedItemStorage.ReferencableEntry<T, ? extends AbstractReferencedItemStorage.ReferenceSetter<T>>
        > {

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
                .filter(entry -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return entry.matches(item);
                })
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one entry: " + a + " vs. " + b);
                })
                .orElseGet(() -> {
                    E newEntry = entryConstructor.apply(item);
                    entries.add(newEntry);
                    return newEntry;
                });
    }

    protected void removeReferenceSettersOwnedBy(Object owner) {
        entries.forEach(entry -> entry.removeReferenceSettersOwnedBy(owner));
    }

    public Map<String, T> buildReferencedItems() {
        Map<String, T> referencedItems = new LinkedHashMap<>();
        entries.stream()
                .filter(entry -> entry.getReferenceSetters().count() > 0) // safe-guard if all reference setters are removed due to ownership
                .filter(entry -> entry.isReferenceRequired() || referenceDecider.turnIntoReference(entry.getItem(), entry.getReferenceSetters().count()))
                .collect(Collectors.groupingBy(this::createNonUniqueReferenceIdentifier))
                .forEach((referenceIdentifier, entriesGroupedByIdentifier) ->
                        buildUniqueReferenceIdentifiers(referenceIdentifier, entriesGroupedByIdentifier).forEach(
                                (uniqueIdentifier, entry) -> {
                                    T referenceItem = itemConstructor.get().createReference(referenceType.getReferencePrefix() + uniqueIdentifier);
                                    T immutableReferenceItem = OpenApiProxyUtils.immutableProxy(referenceItem);
                                    entry.getReferenceSetters().forEach(setter -> setter.consumeReference(immutableReferenceItem));
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

    private Map<String, ReferencableEntry<T, ? extends AbstractReferencedItemStorage.ReferenceSetter<T>>> buildUniqueReferenceIdentifiers(String referenceIdentifier, List<E> entriesGroupedByIdentifier) {
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

    public interface ReferencableEntry<T, R extends ReferenceSetter<T>> {
        T getItem();

        boolean matches(T item);

        default boolean isReferenceRequired() {
            return false;
        }

        @Nullable
        String getSuggestedIdentifier();

        Stream<R> getReferenceSetters();

        void removeReferenceSettersOwnedBy(Object owner);
    }

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static abstract class AbstractReferencableEntryWithReferenceSetter<T, R extends ReferenceSetter<T>> implements ReferencableEntry<T, R> {
        @Getter
        private final T item;
        private final List<R> referenceSetters = new ArrayList<>();

        @Override
        public boolean matches(T item) {
            return this.item.equals(item);
        }

        public void addSetter(R setter) {
            referenceSetters.add(setter);
        }

        public Stream<R> getReferenceSetters() {
            return referenceSetters.stream();
        }

        @Override
        public void removeReferenceSettersOwnedBy(Object owner) {
            // comparing by reference is ok here as the owner is usually mutable,
            // but shouldn't be exchanged by another instance
            referenceSetters.removeIf(referenceSetter -> referenceSetter.getOwner() == owner);
        }
    }

    public static abstract class AbstractReferencableEntry<T> extends AbstractReferencableEntryWithReferenceSetter<T, ReferenceSetter<T>> {

        protected AbstractReferencableEntry(T item) {
            super(item);
        }
    }

    @FunctionalInterface
    public interface ReferenceSetter<T> {

        void consumeReference(T referenceItem);

        @Nullable
        default Object getOwner() {
            return null;
        }
    }
}
