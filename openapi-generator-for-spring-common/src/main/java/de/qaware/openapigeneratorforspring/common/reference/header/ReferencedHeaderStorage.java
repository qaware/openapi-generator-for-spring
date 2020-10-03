package de.qaware.openapigeneratorforspring.common.reference.header;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.header.Header;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public class ReferencedHeaderStorage extends AbstractReferencedItemStorage<Header, ReferencedHeaderStorage.Entry> {

    ReferencedHeaderStorage(ReferenceDeciderForType<Header> referenceDecider, ReferenceIdentifierFactoryForType<Header> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Header> referenceIdentifierConflictResolver) {
        super(ReferenceType.HEADER, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Header::new, Entry::new);
    }

    void storeMaybeReference(String name, Header header, Consumer<Header> setter) {
        getEntryOrAddNew(header)
                .withSuggestedIdentifier(name)
                .addSetter(setter);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Header> {
        protected Entry(Header item) {
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
