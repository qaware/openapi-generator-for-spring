package de.qaware.openapigeneratorforspring.common.reference.component.requestbody;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;

import java.util.Arrays;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.EXAMPLE;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.HEADER;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.SCHEMA;


public class ReferencedRequestBodyStorage extends AbstractReferencedItemStorage<RequestBody> {

    ReferencedRequestBodyStorage(ReferenceDeciderForType<RequestBody> referenceDecider, ReferenceIdentifierFactoryForType<RequestBody> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<RequestBody> referenceIdentifierConflictResolver) {
        super(ReferenceType.REQUEST_BODY, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, RequestBody::new,
                Arrays.asList(SCHEMA, EXAMPLE, HEADER));
    }

    void storeMaybeReference(RequestBody requestBody, Consumer<RequestBody> setter) {
        addEntry(requestBody, setter::accept);
    }
}
