package de.qaware.openapigeneratorforspring.common.reference.requestbody;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

@RequiredArgsConstructor
public class ReferencedRequestBodyHandlerFactory implements ReferencedItemHandlerFactory<RequestBody, RequestBody> {
    private final ReferenceDeciderForRequestBody referenceDecider;
    private final ReferenceIdentifierFactoryForRequestBody referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForRequestBody referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler<RequestBody, RequestBody> create() {
        ReferencedRequestBodyStorage storage = new ReferencedRequestBodyStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedRequestBodyHandlerImpl(storage);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forClass(RequestBody.class);
    }
}
