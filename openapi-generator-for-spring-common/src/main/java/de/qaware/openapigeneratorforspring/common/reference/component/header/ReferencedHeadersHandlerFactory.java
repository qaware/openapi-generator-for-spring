package de.qaware.openapigeneratorforspring.common.reference.component.header;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

@RequiredArgsConstructor
public class ReferencedHeadersHandlerFactory implements ReferencedItemHandlerFactory {
    private final ReferenceDeciderForHeader referenceDecider;
    private final ReferenceIdentifierFactoryForHeader referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForHeader referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedHeaderStorage storage = new ReferencedHeaderStorage(
                referenceDecider,
                (header, headerName) -> Collections.singletonList(referenceIdentifierFactory.buildSuggestedIdentifier(headerName)),
                referenceIdentifierConflictResolver
        );
        return new ReferencedHeadersHandlerImpl(storage, referenceIdentifierFactory);
    }
}
