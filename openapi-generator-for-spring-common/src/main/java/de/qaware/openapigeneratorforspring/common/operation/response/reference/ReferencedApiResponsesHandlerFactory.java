package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemHandlerFactory;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

@RequiredArgsConstructor
public class ReferencedApiResponsesHandlerFactory implements ReferencedItemHandlerFactory<ApiResponses> {

    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolverForApiResponse referenceNameConflictResolver;
    private final ReferenceDeciderForApiResponse referenceDecider;

    @Override
    public ReferencedItemHandler<ApiResponses> create() {
        ReferencedApiResponseStorage storage = new ReferencedApiResponseStorage(referenceNameFactory, referenceNameConflictResolver, referenceDecider);
        return new ReferencedApiResponsesHandlerImpl(storage);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forType(ApiResponses.class);
    }
}
