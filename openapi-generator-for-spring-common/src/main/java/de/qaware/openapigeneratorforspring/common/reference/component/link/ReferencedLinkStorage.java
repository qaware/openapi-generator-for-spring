package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.link.Link;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.HEADER;


public class ReferencedLinkStorage extends AbstractReferencedItemStorage<Link, ReferencedLinkStorage.Entry> {

    ReferencedLinkStorage(ReferenceDeciderForType<Link> referenceDecider, ReferenceIdentifierFactoryForType<Link> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Link> referenceIdentifierConflictResolver) {
        super(ReferenceType.LINK, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Link::new, Entry::new,
                Arrays.asList(HEADER));
    }

    void storeMaybeReferenceLinks(Map<String, Link> links) {
        // no need for ownership as links are referenced at only one place
        // this makes life easier than for example ApiResponses reference handling
        // here, we don't need to track the link name together with the setter,
        // but simpler build a Set of link names inside the entry
        links.forEach((linkName, link) ->
                getEntryOrAddNew(link)
                        .addLinkName(linkName)
                        .addSetter(referenceLink -> links.put(linkName, referenceLink))
        );
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Link> {
        protected Entry(Link item) {
            super(item);
        }

        private final Set<String> linkNames = new LinkedHashSet<>();

        @Override
        public String getSuggestedIdentifier() {
            return String.join("_", linkNames);
        }

        public AbstractReferencableEntry<Link> addLinkName(String linkName) {
            linkNames.add(linkName);
            return this; // fluent API
        }
    }
}
