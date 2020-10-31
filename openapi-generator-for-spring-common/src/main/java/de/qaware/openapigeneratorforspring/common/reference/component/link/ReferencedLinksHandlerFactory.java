package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedLinksHandlerFactory implements ReferencedItemHandlerFactory {
    private final ReferenceDeciderForLink referenceDecider;
    private final ReferenceIdentifierBuilderForLink referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForLink referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedLinkStorage storage = new ReferencedLinkStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedLinksHandlerImpl(storage);
    }
}
