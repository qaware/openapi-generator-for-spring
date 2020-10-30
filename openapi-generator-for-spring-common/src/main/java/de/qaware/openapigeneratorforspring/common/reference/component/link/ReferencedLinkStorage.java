package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.link.Link;

import java.util.Collections;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.HEADER;


public class ReferencedLinkStorage extends AbstractReferencedItemStorage<Link> {

    ReferencedLinkStorage(ReferenceDeciderForType<Link> referenceDecider, ReferenceIdentifierFactoryForType<Link> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Link> referenceIdentifierConflictResolver) {
        super(ReferenceType.LINK, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Link::new,
                Collections.singletonList(HEADER));
    }

    void storeMaybeReferenceLinks(Map<String, Link> links) {
        // no need for ownership as links are referenced at only one place
        // this makes life easier than for example ApiResponses reference handling
        // here, we don't need to track the link name together with the setter,
        // but simpler build a Set of link names inside the entry
        links.forEach((linkName, link) -> addEntry(link, referenceLink -> links.put(linkName, referenceLink), linkName));
    }
}
