package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemHandlerFactory;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

@RequiredArgsConstructor
public class ReferencedApiResponsesHandlerFactory implements ReferencedItemHandlerFactory<ApiResponses> {

    private final ReferenceDeciderForApiResponse referenceDecider;
    private final ReferenceNameFactoryForApiResponse referenceNameFactory;
    private final ReferenceNameConflictResolverForApiResponse referenceNameConflictResolver;

    @Override
    public ReferencedItemHandler<ApiResponses> create() {
        ReferencedApiResponseStorage storage = new ReferencedApiResponseStorage(referenceDecider, referenceNameFactory, referenceNameConflictResolver);
        return new ReferencedApiResponsesHandlerImpl(storage);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forType(ApiResponses.class);
    }
}
