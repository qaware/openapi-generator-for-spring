/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemBuildContext;
import de.qaware.openapigeneratorforspring.common.util.OpenApiProxyUtils;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesTo;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractReferencedItemStorage<T extends HasReference<T>> {

    private final ReferenceType referenceType;
    private final ReferenceDeciderForType<T> referenceDecider;
    private final ReferenceIdentifierBuilderForType<T> referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForType<T> referenceIdentifierConflictResolver;
    private final Supplier<T> itemConstructor;
    private final List<ReferenceType> buildDependencies;

    private final List<Entry<T>> entries = new ArrayList<>();

    public static ReferencedItemBuildContext createContext() {
        return null;
    }

    @Builder
    @Getter(AccessLevel.PRIVATE)
    protected static class AddEntryParameters {
        private final boolean referenceRequired;
        @Nullable
        private final String suggestedIdentifier;
        @Nullable
        private final Object owner;
    }

    protected void addEntriesFromMap(Map<String, T> itemsMap) {
        itemsMap.forEach((itemName, item) -> addEntry(
                item,
                referenceItem -> itemsMap.put(itemName, referenceItem),
                AddEntryParameters.builder()
                        .suggestedIdentifier(itemName)
                        .owner(itemsMap)
                        .build()
        ));
    }

    protected void addEntry(T item, Consumer<T> setter) {
        addEntry(item, setter, AddEntryParameters.builder().build());
    }

    protected void addEntry(T item, Consumer<T> setter, AddEntryParameters options) {
        ReferenceSetterWithIdentifier<T> referenceSetterWithIdentifier = ReferenceSetterWithIdentifier.of(
                ReferenceSetter.of(setter, options.isReferenceRequired()),
                options.getSuggestedIdentifier()
        );
        entries.add(Entry.of(item, referenceSetterWithIdentifier, options.getOwner(), findSetterTarget(setter)));
    }

    @Nullable
    private Object findSetterTarget(Consumer<T> setter) {
        Field[] declaredFields = setter.getClass().getDeclaredFields();
        if (declaredFields.length == 1) {
            Field declaredField = declaredFields[0];
            declaredField.setAccessible(true);
            try {
                return declaredField.get(setter);
            } catch (IllegalAccessException e) {
                // this should not happen, as we've made it accessible...
                return null;
            }
        }
        return null;
    }

    protected void removeEntriesOwnedBy(Object owner) {
        entries.removeIf(entry -> entry.getOwner() == owner);
    }

    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return Pair.of(referenceType, buildDependencies);
    }

    public void buildReferencedItems(Consumer<Map<String, T>> referencedItemsConsumer, @Nullable ReferencedItemBuildContext context) {
        Map<String, T> referencedItems = new LinkedHashMap<>();
        ReferenceActions referenceActions = new ReferenceActions();
        buildGroupedEntries()
                .flatMap(this::buildNonUniqueReferenceIdentifiers)
                .collect(groupingByPairKeyAndCollectingValuesTo(groupingByPairKeyAndCollectingValuesToList()))
                .forEach((referenceIdentifier, itemsGroupedPerIdentifier) -> {
                    removeNotToBeReferencedItems(itemsGroupedPerIdentifier, referenceIdentifier);
                    buildUniqueReferenceIdentifiers(referenceIdentifier, itemsGroupedPerIdentifier).forEach(
                            (uniqueReferenceIdentifier, itemWithReferenceSetters) -> {
                                referencedItems.put(uniqueReferenceIdentifier, itemWithReferenceSetters.getKey());
                                // defer reference action as calling the reference setters will modify items T,
                                // which spoil the usage of the map "itemsGroupedPerIdentifier" where T item is used as the key
                                referenceActions.add(uniqueReferenceIdentifier, itemWithReferenceSetters.getValue());
                            });
                });
        referenceActions.run();
        setMapIfNotEmpty(referencedItems, referencedItemsConsumer);
    }

    private class ReferenceActions extends ArrayList<ReferenceActions.ReferenceAction> {

        public void add(String uniqueIdentifier, List<ReferenceSetter<T>> referenceSetters) {
            add(new ReferenceAction(uniqueIdentifier, referenceSetters));
        }

        public void run() {
            forEach(ReferenceAction::run);
        }

        @RequiredArgsConstructor
        private class ReferenceAction {
            private final String uniqueIdentifier;
            private final List<ReferenceSetter<T>> referenceSetters;

            public void run() {
                T referenceItem = itemConstructor.get().createReference(referenceType.getReferencePrefix() + uniqueIdentifier);
                // make referenceItem immutable as some setters are chained with customizers,
                // which should only apply the the reference items, not to the "dummy" schema where only $ref is set
                T immutableReferenceItem = OpenApiProxyUtils.immutableProxy(referenceItem);
                referenceSetters.forEach(setter -> setter.consumeReference(immutableReferenceItem));
            }
        }
    }

    private void removeNotToBeReferencedItems(Map<T, List<ReferenceSetter<T>>> itemsGroupedByIdentifier, String referenceIdentifier) {
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
        List<ReferenceSetterWithIdentifier<T>> referenceSetters = entry.getReferenceSetters();
        List<IdentifierSetterImpl> identifierSetters = referenceSetters.stream()
                .map(IdentifierSetterImpl::ofReferenceSetter)
                .collect(Collectors.toList());
        referenceIdentifierFactory.buildIdentifiers(entry.getItem(), Collections.unmodifiableList(identifierSetters));
        return IntStream.range(0, identifierSetters.size()).boxed()
                .flatMap(i -> {
                    ReferenceSetter<T> referenceSetter = referenceSetters.get(i).getReferenceSetter();
                    String identifier = identifierSetters.get(i).getValue();
                    if (StringUtils.isBlank(identifier) && referenceSetter.isReferenceRequired()) {
                        throw new IllegalStateException("Cannot skip referencing " + entry.getItem() + " with null identifier as reference is required");
                    } else if (StringUtils.isNotBlank(identifier)) {
                        return Stream.of(Pair.of(identifier, Pair.of(entry.getItem(), referenceSetter)));
                    }
                    return Stream.empty();
                });
    }

    @RequiredArgsConstructor
    @Getter
    private static class IdentifierSetterImpl implements ReferenceIdentifierBuilderForType.IdentifierSetter {

        public static <T> IdentifierSetterImpl ofReferenceSetter(ReferenceSetterWithIdentifier<T> referenceSetter) {
            return new IdentifierSetterImpl(Optional.ofNullable(referenceSetter.getSuggestedIdentifier()), referenceSetter.getReferenceSetter().isReferenceRequired());
        }

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private final Optional<String> suggestedValue;
        private final boolean referenceRequired;

        @Setter
        private String value = null;
    }


    @RequiredArgsConstructor
    @Getter
    private class GroupedEntry {
        private final T item;
        private final List<ReferenceSetterWithIdentifier<T>> referenceSetters = new ArrayList<>();

        public GroupedEntry addReferenceSetter(ReferenceSetterWithIdentifier<T> referenceSetter) {
            referenceSetters.add(referenceSetter);
            return this;
        }

        public GroupedEntry merge(GroupedEntry other) {
            referenceSetters.addAll(other.referenceSetters);
            return this;
        }
    }

    private GroupedEntry createNewGroupedEntry(Entry<T> entry) {
        return new GroupedEntry(entry.getItem()).addReferenceSetter(entry.getReferenceSetterWithIdentifier());
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Entry<T> {
        @EqualsAndHashCode.Include
        private final T item;
        private final ReferenceSetterWithIdentifier<T> referenceSetterWithIdentifier;
        @Nullable
        private final Object owner;
        @Nullable
        private final Object setterTarget;
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class ReferenceSetterWithIdentifier<T> {
        private final ReferenceSetter<T> referenceSetter;
        @Nullable
        private final String suggestedIdentifier;
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class ReferenceSetter<T> {
        private final Consumer<T> setter;
        private final boolean referenceRequired;

        void consumeReference(T referenceItem) {
            setter.accept(referenceItem);
        }
    }
}
