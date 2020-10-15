package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedSchemaHandlerFactory implements ReferencedItemHandlerFactory {

    private final ReferenceDeciderForSchema referenceDecider;
    private final ReferenceIdentifierFactoryForSchema referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForSchema referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedSchemaStorage storage = new ReferencedSchemaStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedSchemaHandlerImpl(storage);
    }

}
