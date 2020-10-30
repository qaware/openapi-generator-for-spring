package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.common.util.OpenApiProxyUtils;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.nullOr;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesTo;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractReferencedItemStorage<
        T extends HasReference<T>,
        E extends AbstractReferencedItemStorage.ReferencableEntry<T, ? extends AbstractReferencedItemStorage.ReferenceSetter<T>>
        > {

    private final ReferenceType referenceType;
    private final ReferenceDeciderForType<T> referenceDecider;
    private final ReferenceIdentifierFactoryForType<T> referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForType<T> referenceIdentifierConflictResolver;
    private final Supplier<T> itemConstructor;
    private final Function<T, E> entryConstructor;
    private final List<ReferenceType> buildDependencies;

    private final List<E> entries = new ArrayList<>();

    protected E getEntryOrAddNew(T item) {
        // we don't use a map here to be able to recompute grouping
        // when items of already stored entries get altered via reference!
        return entries.stream()
                .filter(entry -> entry.getItem().equals(item))
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

    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return Pair.of(referenceType, buildDependencies);
    }

    public Map<String, T> buildReferencedItems() {
        Map<String, T> referencedItems = new LinkedHashMap<>();
        entries.stream()
                .filter(entry -> entry.getReferenceSetters().count() > 0) // safe-guard if all reference setters are removed due to ownership
                .filter(entry -> entry.isReferenceRequired() || referenceDecider.turnIntoReference(entry.getItem(), entry.getReferenceSetters().count()))
                .flatMap(this::buildNonUniqueReferenceIdentifier)
                .collect(groupingByPairKeyAndCollectingValuesTo(groupingByPairKeyAndCollectingValuesToList()))
                .forEach((referenceIdentifier, entriesMapGroupedByIdentifier) ->
                        buildUniqueReferenceIdentifiers(referenceIdentifier, entriesMapGroupedByIdentifier).forEach(
                                (uniqueIdentifier, entry) -> {
                                    T referenceItem = itemConstructor.get().createReference(referenceType.getReferencePrefix() + uniqueIdentifier);
                                    T immutableReferenceItem = OpenApiProxyUtils.immutableProxy(referenceItem);
                                    entry.getReferenceSetters().forEach(setter -> setter.consumeReference(immutableReferenceItem));
                                    referencedItems.put(uniqueIdentifier, entry.getItem());
                                }));
        return referencedItems;
    }

    private Map<String, ConsumableEntry> buildUniqueReferenceIdentifiers(String referenceIdentifier, Map<UniqueEntry, List<ReferenceSetter<T>>> entriesMapGroupedByIdentifier) {
        if (entriesMapGroupedByIdentifier.size() == 1) {
            // there's no conflict if there's only one entry to be consumed,
            // then just return the identifier itself
            return Collections.singletonMap(
                    referenceIdentifier,
                    createConsumableEntry(entriesMapGroupedByIdentifier.entrySet().iterator().next())
            );
        }

        List<T> itemsWithSameIdentifier = entriesMapGroupedByIdentifier.keySet().stream()
                .map(UniqueEntry::getItem)
                .collect(Collectors.toList());

        List<String> uniqueIdentifiers = referenceIdentifierConflictResolver.resolveConflict(itemsWithSameIdentifier, referenceIdentifier);
        if (uniqueIdentifiers.size() != itemsWithSameIdentifier.size()) {
            throw new IllegalStateException(String.format("Conflict resolver %s did not return expected number %d but %d unique reference identifiers for items referenced by %s",
                    referenceIdentifierConflictResolver.getClass().getSimpleName(), itemsWithSameIdentifier.size(), uniqueIdentifiers.size(), referenceIdentifier));
        }

        // zip unique identifiers with map entries
        List<Map.Entry<UniqueEntry, List<ReferenceSetter<T>>>> entriesMapAsList = new ArrayList<>(entriesMapGroupedByIdentifier.entrySet());
        return IntStream.range(0, entriesMapAsList.size()).boxed()
                .collect(Collectors.toMap(
                        uniqueIdentifiers::get,
                        i -> createConsumableEntry(entriesMapAsList.get(i)),
                        (a, b) -> {
                            throw new IllegalStateException(String.format("Found non-unique reference identifier from conflict resolver %s: %s vs. %s",
                                    referenceIdentifierConflictResolver.getClass().getSimpleName(), a, b));
                        },
                        LinkedHashMap::new
                ));
    }

    private Stream<Pair<String, Pair<UniqueEntry, ReferenceSetter<T>>>> buildNonUniqueReferenceIdentifier(E entry) {
        List<Pair<String, Pair<UniqueEntry, ReferenceSetter<T>>>> result = new ArrayList<>();
        // intermediate grouping ensures we don't call the referenceIdentifierFactory not more than needed!
        entry.getReferenceSetters()
                .map(referenceSetter -> {
                    // suggestedIdentifier can still be null, as the entry might also not know one
                    // so we need to wrap it inside SuggestedIdentifier to handle the null value when grouping
                    String suggestedIdentifier = nullOr(referenceSetter.getSuggestedIdentifier(), entry.getSuggestedIdentifier());
                    return Pair.of(SuggestedIdentifier.of(suggestedIdentifier), referenceSetter);
                })
                .collect(groupingByPairKeyAndCollectingValuesToList())
                .forEach(((suggestedIdentifier, referenceSetters) -> {
                    String identifier = referenceIdentifierFactory.buildIdentifier(entry.getItem(), suggestedIdentifier.getValue());
                    UniqueEntry uniqueEntry = new UniqueEntry(entry);
                    referenceSetters.forEach(referenceSetter -> result.add(Pair.of(identifier, Pair.of(uniqueEntry, referenceSetter))));
                }));
        return result.stream();
    }

    @RequiredArgsConstructor(staticName = "of")
    @EqualsAndHashCode // important for grouping!
    @Getter
    private static class SuggestedIdentifier {
        @Nullable
        private final String value;
    }

    @RequiredArgsConstructor
    @Getter
    private class ConsumableEntry {
        private final T item;
        private final List<ReferenceSetter<T>> referenceSetters;
    }

    private ConsumableEntry createConsumableEntry(Map.Entry<UniqueEntry, List<ReferenceSetter<T>>> entry) {
        // note however that the reference setters in this onlyEntry may only be a subset
        // of all reference setters belonging to this unique entry!
        return new ConsumableEntry(entry.getKey().getItem(), entry.getValue());
    }

    @RequiredArgsConstructor
    private class UniqueEntry {
        private final E entry;

        public T getItem() {
            return entry.getItem();
        }
        // equals/hashCode intentionally not implemented to make groupingBy work correctly
    }

    public interface ReferencableEntry<T, R extends ReferenceSetter<T>> {
        T getItem();

        default boolean isReferenceRequired() {
            return false;
        }

        @Nullable
        default String getSuggestedIdentifier() {
            return null;
        }

        Stream<R> getReferenceSetters();

        void removeReferenceSettersOwnedBy(Object owner);
    }

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static abstract class AbstractReferencableEntryWithReferenceSetter<T, R extends ReferenceSetter<T>> implements ReferencableEntry<T, R> {
        @Getter
        private final T item;
        private final List<R> referenceSetters = new ArrayList<>();

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
        default String getSuggestedIdentifier() {
            return null;
        }

        @Nullable
        default Object getOwner() {
            return null;
        }
    }
}
