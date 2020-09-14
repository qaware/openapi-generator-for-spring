package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import io.swagger.v3.oas.models.examples.Example;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ReferencedExampleStorage extends AbstractReferencedItemStorage<Example, ReferencedExampleStorage.Entry> {

    ReferencedExampleStorage(ReferenceDeciderForType<Example> referenceDecider, ReferenceNameFactoryForType<Example> referenceNameFactory, ReferenceNameConflictResolverForType<Example> referenceNameConflictResolver) {
        super(referenceDecider, referenceNameFactory, referenceNameConflictResolver, Entry::new);
    }

    void storeMaybeReference(String name, Example example, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(example)
                .withName(name)
                .addSetter(referenceNameConsumer);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Example> {
        protected Entry(Example item) {
            super(item);
        }

        @Nullable
        private String name;

        public Entry withName(String name) {
            this.name = name;
            return this; // fluent API
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return name;
        }
    }
}
