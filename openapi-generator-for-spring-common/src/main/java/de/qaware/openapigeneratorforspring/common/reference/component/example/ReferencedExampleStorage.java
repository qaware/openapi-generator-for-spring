package de.qaware.openapigeneratorforspring.common.reference.component.example;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.example.Example;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.EXAMPLE;

public class ReferencedExampleStorage extends AbstractReferencedItemStorage<Example, ReferencedExampleStorage.Entry> {

    ReferencedExampleStorage(ReferenceDeciderForType<Example> referenceDecider, ReferenceIdentifierFactoryForType<Example> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Example> referenceIdentifierConflictResolver) {
        super(EXAMPLE, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Example::new, Entry::new,
                Collections.emptyList());
    }

    void storeMaybeReference(String name, Example example, Consumer<Example> setter) {
        getEntryOrAddNew(example)
                .withSuggestedIdentifier(name)
                .addSetter(setter::accept);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Example> {
        protected Entry(Example item) {
            super(item);
        }

        @Nullable
        @Getter
        private String suggestedIdentifier;

        public Entry withSuggestedIdentifier(String name) {
            this.suggestedIdentifier = name;
            return this; // fluent API
        }
    }
}
