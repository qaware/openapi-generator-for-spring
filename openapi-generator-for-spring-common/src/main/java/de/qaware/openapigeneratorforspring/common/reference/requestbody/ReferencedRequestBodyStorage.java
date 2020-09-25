package de.qaware.openapigeneratorforspring.common.reference.requestbody;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import io.swagger.v3.oas.models.parameters.RequestBody;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public class ReferencedRequestBodyStorage extends AbstractReferencedItemStorage<RequestBody, ReferencedRequestBodyStorage.Entry> {

    ReferencedRequestBodyStorage(ReferenceDeciderForType<RequestBody> referenceDecider, ReferenceNameFactoryForType<RequestBody> referenceNameFactory, ReferenceNameConflictResolverForType<RequestBody> referenceNameConflictResolver) {
        super(referenceDecider, referenceNameFactory, referenceNameConflictResolver, Entry::new);
    }

    void storeMaybeReference(RequestBody example, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(example).addSetter(referenceNameConsumer);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<RequestBody> {
        protected Entry(RequestBody item) {
            super(item);
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            // TODO is there are good way for request body reference name?
            return null;
        }
    }
}
