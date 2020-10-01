package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

@RequiredArgsConstructor
public class ReferencedApiResponsesHandlerFactory implements ReferencedItemHandlerFactory<ApiResponses, ApiResponses> {

    private final ReferenceDeciderForApiResponse referenceDecider;
    private final ReferenceIdentifierFactoryForApiResponse referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForApiResponse referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler<ApiResponses, ApiResponses> create() {
        ReferencedApiResponseStorage storage = new ReferencedApiResponseStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedApiResponsesHandlerImpl(storage);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forType(ApiResponses.class);
    }
}
