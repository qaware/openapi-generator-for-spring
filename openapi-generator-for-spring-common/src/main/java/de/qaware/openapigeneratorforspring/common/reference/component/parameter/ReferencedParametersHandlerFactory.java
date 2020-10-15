package de.qaware.openapigeneratorforspring.common.reference.component.parameter;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedParametersHandlerFactory implements ReferencedItemHandlerFactory {
    private final ReferenceDeciderForParameter referenceDecider;
    private final ReferenceIdentifierFactoryForParameter referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForParameter referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedParameterStorage storage = new ReferencedParameterStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedParametersHandlerImpl(storage, null);
    }
}
