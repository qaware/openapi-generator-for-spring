package de.qaware.openapigeneratorforspring.common.reference;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractReferencedItemStorage<T, E extends AbstractReferencedItemStorage.ReferencableEntry<T>> {

    private static final String IDENTIFIER_SEPARATOR = "_";

    private final ReferenceDeciderForType<T> referenceDecider;
    private final ReferenceNameFactoryForType<T> referenceNameFactory;
    private final ReferenceNameConflictResolverForType<T> referenceNameConflictResolver;
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
        Map<String, T> referencedApiResponses = new HashMap<>();
        entries.stream()
                .filter(entry -> entry.isReferenceRequired() || referenceDecider.turnIntoReference(entry.getItem(), entry.getNumberOfUsages()))
                .collect(Collectors.groupingBy(this::createNonUniqueReferenceName))
                .forEach((referenceName, entriesGroupedByReferenceName) ->
                        buildUniqueReferenceNames(referenceName, entriesGroupedByReferenceName).forEach(
                                (uniqueReferenceName, entry) -> {
                                    entry.getReferenceNameSetters().forEach(setter -> setter.accept(uniqueReferenceName));
                                    referencedApiResponses.put(uniqueReferenceName.getIdentifier(), entry.getItem());
                                })
                );
        return referencedApiResponses;
    }

    private ReferenceName createNonUniqueReferenceName(E entry) {
        String identifier = referenceNameFactory.buildIdentifierComponents(entry.getItem(), entry.getSuggestedIdentifier()).stream()
                .map(Object::toString)
                .collect(Collectors.joining(IDENTIFIER_SEPARATOR));
        ReferenceName.Type type = referenceNameFactory.getReferenceNameType();
        // at this point, the identifier may not be unique
        return new ReferenceName(type, identifier);
    }

    private Map<ReferenceName, ReferencableEntry<T>> buildUniqueReferenceNames(ReferenceName referenceName, List<E> entriesGroupedByReferenceName) {
        if (entriesGroupedByReferenceName.size() == 1) {
            // special case: no conflicts need to be resolved as there's only one item for this reference name
            return Collections.singletonMap(referenceName, entriesGroupedByReferenceName.get(0));
        }

        List<T> itemsWithSameReferenceName = entriesGroupedByReferenceName.stream()
                .map(ReferencableEntry::getItem)
                .collect(Collectors.toList());

        List<ReferenceName> uniqueReferenceNames = referenceNameConflictResolver.resolveConflict(itemsWithSameReferenceName, referenceName);
        if (uniqueReferenceNames.size() != itemsWithSameReferenceName.size()) {
            throw new IllegalStateException(String.format("Conflict resolver %s did not return expected number %d but %d unique reference names for items referenced by %s",
                    referenceNameConflictResolver.getClass().getSimpleName(), itemsWithSameReferenceName.size(), uniqueReferenceNames.size(), referenceName));
        }

        // zip items into map
        return IntStream.range(0, entriesGroupedByReferenceName.size()).boxed()
                .collect(Collectors.toMap(
                        uniqueReferenceNames::get,
                        entriesGroupedByReferenceName::get,
                        (a, b) -> {
                            throw new IllegalStateException(String.format("Found non-unique reference name from conflict resolver %s: %s vs. %s",
                                    referenceNameConflictResolver.getClass().getSimpleName(), a, b));
                        })
                );
    }

    public interface ReferencableEntry<T> {
        T getItem();

        boolean matches(T item);

        default boolean isReferenceRequired() {
            return false;
        }

        int getNumberOfUsages();

        @Nullable
        String getSuggestedIdentifier();

        Stream<Consumer<ReferenceName>> getReferenceNameSetters();

    }
}
