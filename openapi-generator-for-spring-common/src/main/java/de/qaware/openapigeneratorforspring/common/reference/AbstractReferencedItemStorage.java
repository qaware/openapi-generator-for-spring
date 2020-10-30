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
import java.util.Iterator;
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
                .flatMap(this::buildNonUniqueReferenceIdentifiers)
                .collect(groupingByPairKeyAndCollectingValuesTo(groupingByPairKeyAndCollectingValuesToList()))
                .forEach((referenceIdentifier, itemsPerIdentifier) -> {
                    removeNotToBeReferencedItems(referenceIdentifier, itemsPerIdentifier);
                    buildUniqueReferenceIdentifiers(referenceIdentifier, itemsPerIdentifier).forEach(
                            (uniqueIdentifier, itemWithReferenceSetters) -> {
                                T referenceItem = itemConstructor.get().createReference(referenceType.getReferencePrefix() + uniqueIdentifier);
                                T immutableReferenceItem = OpenApiProxyUtils.immutableProxy(referenceItem);
                                itemWithReferenceSetters.getValue().forEach(setter -> setter.consumeReference(immutableReferenceItem));
                                referencedItems.put(uniqueIdentifier, itemWithReferenceSetters.getKey());
                            });
                });
        return referencedItems;
    }

    private void removeNotToBeReferencedItems(String referenceIdentifier, Map<T, List<ReferenceSetter<T>>> itemsGroupedByIdentifier) {
        Iterator<Map.Entry<T, List<ReferenceSetter<T>>>> iterator = itemsGroupedByIdentifier.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<T, List<ReferenceSetter<T>>> entry = iterator.next();
            boolean isReferenceNotRequired = entry.getValue().stream().noneMatch(ReferenceSetter::isReferenceRequired);
            if (isReferenceNotRequired && !referenceDecider.turnIntoReference(entry.getKey(), referenceIdentifier, entry.getValue().size())) {
                iterator.remove();
            }
        }
    }

    private Stream<GroupedEntry> buildGroupedEntries() {
        return entries.stream()
                .collect(Collectors.toMap(e -> e, this::createNewGroupedEntry, GroupedEntry::merge, LinkedHashMap::new))
                .values().stream();
    }

    private Map<String, Map.Entry<T, List<ReferenceSetter<T>>>> buildUniqueReferenceIdentifiers(String referenceIdentifier, Map<T, List<ReferenceSetter<T>>> itemsGroupedByIdentifier) {
        if (itemsGroupedByIdentifier.size() == 1) {
            // there's no conflict if there's only one item to be consumed,
            // then just return the identifier itself
            return Collections.singletonMap(
                    referenceIdentifier,
                    itemsGroupedByIdentifier.entrySet().iterator().next()
            );
        }

        List<T> itemsWithSameIdentifier = new ArrayList<>(itemsGroupedByIdentifier.keySet());

        List<String> uniqueIdentifiers = referenceIdentifierConflictResolver.resolveConflict(itemsWithSameIdentifier, referenceIdentifier);
        if (uniqueIdentifiers.size() != itemsWithSameIdentifier.size()) {
            throw new IllegalStateException(String.format("Conflict resolver %s did not return expected number %d but %d unique reference identifiers for items referenced by %s",
                    referenceIdentifierConflictResolver.getClass().getSimpleName(), itemsWithSameIdentifier.size(), uniqueIdentifiers.size(), referenceIdentifier));
        }

        // zip unique identifiers with map entries
        List<Map.Entry<T, List<ReferenceSetter<T>>>> entriesMapAsList = new ArrayList<>(itemsGroupedByIdentifier.entrySet());
        return IntStream.range(0, entriesMapAsList.size()).boxed()
                .collect(Collectors.toMap(
                        uniqueIdentifiers::get,
                        entriesMapAsList::get,
                        (a, b) -> {
                            throw new IllegalStateException(String.format("Found non-unique reference identifier from conflict resolver %s: %s vs. %s",
                                    referenceIdentifierConflictResolver.getClass().getSimpleName(), a, b));
                        },
                        LinkedHashMap::new
                ));
    }

    private Stream<Pair<String, Pair<T, ReferenceSetter<T>>>> buildNonUniqueReferenceIdentifiers(GroupedEntry entry) {
        List<Pair<String, Pair<T, ReferenceSetter<T>>>> result = new ArrayList<>();
        entry.getReferenceSetters().stream()
                // intermediate grouping by suggestedIdentifier ensures we don't call the referenceIdentifierFactory not more than needed!
                .collect(groupingByPairKeyAndCollectingValuesToList())
                .forEach(((suggestedIdentifier, referenceSetters) -> {
                    String identifier = referenceIdentifierFactory.buildIdentifier(entry.getItem(), suggestedIdentifier.getValue(), referenceSetters.size());
                    boolean isReferenceRequired = referenceSetters.stream().anyMatch(ReferenceSetter::isReferenceRequired);
                    if (identifier == null && isReferenceRequired) {
                        throw new IllegalStateException("Cannot skip referencing " + entry.getItem() + " with null identifier as reference is required for at least one reference setter");
                    } else if (identifier != null) {
                        referenceSetters.forEach(referenceSetter -> result.add(Pair.of(identifier, Pair.of(entry.getItem(), referenceSetter))));
                    }
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
    private class GroupedEntry {
        private final T item;
        private final List<Pair<SuggestedIdentifier, ReferenceSetter<T>>> referenceSetters = new ArrayList<>();

        public GroupedEntry addReferenceSetter(Pair<SuggestedIdentifier, ReferenceSetter<T>> referenceSetter) {
            referenceSetters.add(referenceSetter);
            return this;
        }

        public GroupedEntry merge(GroupedEntry other) {
            referenceSetters.addAll(other.referenceSetters);
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
