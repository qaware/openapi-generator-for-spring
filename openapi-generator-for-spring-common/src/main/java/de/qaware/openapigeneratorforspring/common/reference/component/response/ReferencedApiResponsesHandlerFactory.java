package de.qaware.openapigeneratorforspring.common.reference.component.response;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedApiResponsesHandlerFactory implements ReferencedItemHandlerFactory {

    private final ReferenceDeciderForApiResponse referenceDecider;
    private final ReferenceIdentifierBuilderForApiResponse referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForApiResponse referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedApiResponseStorage storage = new ReferencedApiResponseStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedApiResponsesHandlerImpl(storage);
    }

}
