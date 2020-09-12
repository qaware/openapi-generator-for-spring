package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class DefaultReferencedParameterStorage implements ReferencedParameterStorage {
    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolverForParameter referenceNameConflictResolver;
    private final ReferenceDeciderForParameter referenceDecider;

    private final List<EntryWithSetters> entries = new ArrayList<>();

    @Override
    public void storeParameterMaybeReference(Parameter item, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(item).getReferenceNameSetters().add(referenceNameConsumer);
    }

    private EntryWithSetters getEntryOrAddNew(Parameter item) {
        return entries.stream()
                .filter(entry -> entry.matchesItem(item))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one entry: " + a + " vs. " + b);
                })
                .orElseGet(() -> new EntryWithSetters(item)); // constructor registers it in entries as well
    }

    @Override
    public Map<String, Parameter> buildReferencedParameters() {
        Map<String, Parameter> referencedItems = new HashMap<>();
        entries.stream()
                .filter(entry -> referenceDecider.turnIntoReference(entry.getItem(), entry.getReferenceNameSetters().size()))
                .collect(Collectors.groupingBy(entry -> referenceNameFactory.create(entry.getItem(), entry.getSuggestedIdentifier())))
                .forEach((referenceName, groupedItemsWithSetters) ->
                        buildUniqueReferenceNames(groupedItemsWithSetters, referenceName).forEach(
                                (uniqueReferenceName, groupedEntries) -> {
                                    List<Consumer<ReferenceName>> referenceNameSetters = groupedEntries.getReferenceNameSetters();
                                    referenceNameSetters.forEach(setter -> setter.accept(uniqueReferenceName));
                                    referencedItems.put(uniqueReferenceName.getIdentifier(), groupedEntries.getItem());
                                })
                );
        return referencedItems;
    }

    private Map<ReferenceName, EntryWithSetters> buildUniqueReferenceNames(List<EntryWithSetters> groupedEntries, ReferenceName referenceName) {
        if (groupedEntries.size() == 1) {
            // special case: no conflicts need to be resolved as there's only one item for this reference name
            return Collections.singletonMap(referenceName, groupedEntries.get(0));
        }

        List<Parameter> itemsWithSameReferenceName = groupedEntries.stream()
                .map(EntryWithSetters::getItem)
                .collect(Collectors.toList());

        List<ReferenceName> uniqueReferenceNames = referenceNameConflictResolver.resolveConflict(itemsWithSameReferenceName, referenceName);
        if (uniqueReferenceNames.size() != itemsWithSameReferenceName.size()) {
            throw new IllegalStateException(String.format("Conflict resolver %s did not return expected number %d but %d unique reference names for items referenced by %s",
                    referenceNameConflictResolver.getClass().getSimpleName(), itemsWithSameReferenceName.size(), uniqueReferenceNames.size(), referenceName));
        }

        // zip lists uniqueReferenceNames and groupedEntries into map
        return IntStream.range(0, groupedEntries.size()).boxed()
                .collect(Collectors.toMap(
                        uniqueReferenceNames::get,
                        groupedEntries::get,
                        (a, b) -> {
                            throw new IllegalStateException(String.format("Found non-unique reference name from conflict resolver %s: %s vs. %s",
                                    referenceNameConflictResolver.getClass().getSimpleName(), a, b));
                        })
                );
    }

    @Getter
    private class EntryWithSetters {

        private final Parameter item;
        private final List<Consumer<ReferenceName>> referenceNameSetters = new ArrayList<>();

        public boolean matchesItem(Parameter otherItem) {
            return item.equals(otherItem);
        }

        public EntryWithSetters(Parameter item) {
            this.item = item;
            // register instance in parent class
            entries.add(this);
        }

        public String getSuggestedIdentifier() {
            return item.getName();
        }

        @Override
        public String toString() {
            return String.format("Entry with %s setters for %s", referenceNameSetters.size(), item);
        }
    }
}
