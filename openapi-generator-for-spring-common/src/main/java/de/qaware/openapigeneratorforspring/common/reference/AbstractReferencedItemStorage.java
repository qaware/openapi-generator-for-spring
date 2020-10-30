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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesTo;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractReferencedItemStorage<T extends HasReference<T>> {

    private final ReferenceType referenceType;
    private final ReferenceDeciderForType<T> referenceDecider;
    private final ReferenceIdentifierFactoryForType<T> referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForType<T> referenceIdentifierConflictResolver;
    private final Supplier<T> itemConstructor;
    private final List<ReferenceType> buildDependencies;

    private final List<Entry<T>> entries = new ArrayList<>();

    protected void addEntry(T item, ReferenceSetter<T> referenceSetter) {
        addEntry(item, referenceSetter, null, null);
    }

    protected void addEntry(T item, ReferenceSetter<T> referenceSetter, @Nullable String suggestedIdentifier) {
        addEntry(item, referenceSetter, suggestedIdentifier, null);
    }

    protected void addEntry(T item, ReferenceSetter<T> referenceSetter, @Nullable String suggestedIdentifier, @Nullable Object owner) {
        entries.add(Entry.of(item, referenceSetter, SuggestedIdentifier.of(suggestedIdentifier), owner));
    }

    protected void removeEntriesOwnedBy(Object owner) {
        entries.removeIf(entry -> entry.getOwner() == owner);
    }

    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return Pair.of(referenceType, buildDependencies);
    }

    public Map<String, T> buildReferencedItems() {
        Map<String, T> referencedItems = new LinkedHashMap<>();
        buildGroupedEntries()
                .filter(entry -> entry.getReferenceSetters().size() > 0) // safe-guard if all reference setters are removed due to ownership
                .filter(entry -> entry.isReferenceRequired() || referenceDecider.turnIntoReference(entry.getItem(), entry.getReferenceSetters().size()))
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

    private Stream<GroupedEntry> buildGroupedEntries() {
        return entries.stream()
                .collect(Collectors.toMap(e -> e, this::createNewGroupedEntry, GroupedEntry::merge, LinkedHashMap::new))
                .values().stream();
    }

    private Map<String, ConsumableEntry> buildUniqueReferenceIdentifiers(String referenceIdentifier, Map<GroupedEntry, List<ReferenceSetter<T>>> entriesMapGroupedByIdentifier) {
        if (entriesMapGroupedByIdentifier.size() == 1) {
            // there's no conflict if there's only one entry to be consumed,
            // then just return the identifier itself
            return Collections.singletonMap(
                    referenceIdentifier,
                    createConsumableEntry(entriesMapGroupedByIdentifier.entrySet().iterator().next())
            );
        }

        List<T> itemsWithSameIdentifier = entriesMapGroupedByIdentifier.keySet().stream()
                .map(GroupedEntry::getItem)
                .collect(Collectors.toList());

        List<String> uniqueIdentifiers = referenceIdentifierConflictResolver.resolveConflict(itemsWithSameIdentifier, referenceIdentifier);
        if (uniqueIdentifiers.size() != itemsWithSameIdentifier.size()) {
            throw new IllegalStateException(String.format("Conflict resolver %s did not return expected number %d but %d unique reference identifiers for items referenced by %s",
                    referenceIdentifierConflictResolver.getClass().getSimpleName(), itemsWithSameIdentifier.size(), uniqueIdentifiers.size(), referenceIdentifier));
        }

        // zip unique identifiers with map entries
        List<Map.Entry<GroupedEntry, List<ReferenceSetter<T>>>> entriesMapAsList = new ArrayList<>(entriesMapGroupedByIdentifier.entrySet());
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

    private Stream<Pair<String, Pair<GroupedEntry, ReferenceSetter<T>>>> buildNonUniqueReferenceIdentifier(GroupedEntry entry) {
        List<Pair<String, Pair<GroupedEntry, ReferenceSetter<T>>>> result = new ArrayList<>();
        // intermediate grouping ensures we don't call the referenceIdentifierFactory not more than needed!
        entry.getReferenceSetters().stream()
                .collect(groupingByPairKeyAndCollectingValuesToList())
                .forEach(((suggestedIdentifier, referenceSetters) -> {
                    String identifier = referenceIdentifierFactory.buildIdentifier(entry.getItem(), suggestedIdentifier.getValue());
                    referenceSetters.forEach(referenceSetter -> result.add(Pair.of(identifier, Pair.of(entry, referenceSetter))));
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

    private ConsumableEntry createConsumableEntry(Map.Entry<GroupedEntry, List<ReferenceSetter<T>>> entry) {
        // note however that the reference setters in entry.getValue() may only be a subset
        // of all reference setters belonging to this GroupedEntry!
        return new ConsumableEntry(entry.getKey().getItem(), entry.getValue());
    }

    @RequiredArgsConstructor
    @Getter
    private class GroupedEntry {
        private final T item;
        private final List<Pair<SuggestedIdentifier, ReferenceSetter<T>>> referenceSetters = new ArrayList<>();
        private boolean referenceRequired = false;

        public GroupedEntry addReferenceSetter(Pair<SuggestedIdentifier, ReferenceSetter<T>> referenceSetter) {
            referenceSetters.add(referenceSetter);
            if (referenceSetter.getValue().isReferenceRequired()) {
                referenceRequired = true;
            }
            return this;
        }

        public GroupedEntry merge(GroupedEntry other) {
            // important not to use addAll to keep track of referenceRequired flag
            other.referenceSetters.forEach(this::addReferenceSetter);
            return this;
        }
    }

    private GroupedEntry createNewGroupedEntry(Entry<T> entry) {
        return new GroupedEntry(entry.getItem()).addReferenceSetter(Pair.of(entry.getSuggestedIdentifier(), entry.getReferenceSetter()));
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class Entry<T> {
        private final T item;
        private final ReferenceSetter<T> referenceSetter;
        private final SuggestedIdentifier suggestedIdentifier;
        @Nullable
        private final Object owner;

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Entry<?> && item.equals(((Entry<?>) obj).item);
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }

    @FunctionalInterface
    public interface ReferenceSetter<T> {

        void consumeReference(T referenceItem);

        default boolean isReferenceRequired() {
            return false;
        }
    }
}
