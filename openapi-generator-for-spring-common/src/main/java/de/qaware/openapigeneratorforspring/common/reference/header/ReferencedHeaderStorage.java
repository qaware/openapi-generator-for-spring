package de.qaware.openapigeneratorforspring.common.reference.header;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import io.swagger.v3.oas.models.headers.Header;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public class ReferencedHeaderStorage extends AbstractReferencedItemStorage<Header, ReferencedHeaderStorage.Entry> {

    ReferencedHeaderStorage(ReferenceDeciderForType<Header> referenceDecider, ReferenceNameFactoryForType<Header> referenceNameFactory, ReferenceNameConflictResolverForType<Header> referenceNameConflictResolver) {
        super(referenceDecider, referenceNameFactory, referenceNameConflictResolver, Entry::new);
    }

    void storeMaybeReference(String name, Header header, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(header)
                .withSuggestedIdentifier(name)
                .addSetter(referenceNameConsumer);
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
