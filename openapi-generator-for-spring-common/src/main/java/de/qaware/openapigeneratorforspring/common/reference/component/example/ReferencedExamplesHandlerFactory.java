package de.qaware.openapigeneratorforspring.common.reference.component.example;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedExamplesHandlerFactory implements ReferencedItemHandlerFactory {
    private final ReferenceDeciderForExample referenceDecider;
    private final ReferenceIdentifierFactoryForExample referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForExample referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler create() {
        ReferencedExampleStorage storage = new ReferencedExampleStorage(
                referenceDecider,
                (example, exampleName, numberOfSetters) -> referenceIdentifierFactory.buildSuggestedIdentifier(exampleName),
                referenceIdentifierConflictResolver
        );
        return new ReferencedExamplesHandlerImpl(storage, referenceIdentifierFactory);
    }
}
