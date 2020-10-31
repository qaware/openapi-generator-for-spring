package de.qaware.openapigeneratorforspring.common.reference.component.callback;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedCallbacksHandlerFactory implements ReferencedItemHandlerFactory {
    private final ReferenceDeciderForCallback referenceDecider;
    private final ReferenceIdentifierBuilderForCallback referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForCallback referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedCallbackStorage storage = new ReferencedCallbackStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedCallbacksHandlerImpl(storage);
    }
}
