package de.qaware.openapigeneratorforspring.common.reference.component.header;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedHeadersHandlerFactory implements ReferencedItemHandlerFactory {
    private final ReferenceDeciderForHeader referenceDecider;
    private final ReferenceIdentifierFactoryForHeader referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForHeader referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedHeaderStorage storage = new ReferencedHeaderStorage(
                referenceDecider,
                (header, headerName) -> referenceIdentifierFactory.buildSuggestedIdentifier(headerName),
                referenceIdentifierConflictResolver
        );
        return new ReferencedHeadersHandlerImpl(storage, referenceIdentifierFactory);
    }
}
