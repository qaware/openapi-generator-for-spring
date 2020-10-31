package de.qaware.openapigeneratorforspring.common.reference.component.header;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.header.Header;

import java.util.Arrays;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.EXAMPLE;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.HEADER;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.SCHEMA;


public class ReferencedHeaderStorage extends AbstractReferencedItemStorage<Header> {

    ReferencedHeaderStorage(ReferenceDeciderForType<Header> referenceDecider, ReferenceIdentifierBuilderForType<Header> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Header> referenceIdentifierConflictResolver) {
        super(HEADER, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Header::new,
                Arrays.asList(HEADER, SCHEMA, EXAMPLE));
    }

    public void maybeReferenceHeaders(Map<String, Header> headers) {
        addEntriesFromMap(headers);
    }
}
