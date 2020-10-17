package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedLinksHandlerFactory implements ReferencedItemHandlerFactory {
    private final ReferenceDeciderForLink referenceDecider;
    private final ReferenceIdentifierFactoryForLink referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForLink referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedLinksStorage storage = new ReferencedLinksStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedLinksHandlerImpl(storage);
    }
}
