package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.link.Link;

import java.util.Collections;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.HEADER;


public class ReferencedLinkStorage extends AbstractReferencedItemStorage<Link> {
    ReferencedLinkStorage(ReferenceDeciderForType<Link> referenceDecider, ReferenceIdentifierBuilderForType<Link> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Link> referenceIdentifierConflictResolver) {
        super(ReferenceType.LINK, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Link::new,
                Collections.singletonList(HEADER));
    }

    public void maybeReferenceLinks(Map<String, Link> links) {
        addEntriesFromMap(links);
    }
}
