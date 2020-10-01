package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

@RequiredArgsConstructor
public class ReferencedSchemaHandlerFactory implements ReferencedItemHandlerFactory<Schema, Schema> {

    private final ReferenceDeciderForSchema referenceDecider;
    private final ReferenceIdentifierFactoryForSchema referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForSchema referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler<Schema, Schema> create() {
        ReferencedSchemaStorage storage = new ReferencedSchemaStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedSchemaHandlerImpl(storage);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forClass(Schema.class);
    }
}
