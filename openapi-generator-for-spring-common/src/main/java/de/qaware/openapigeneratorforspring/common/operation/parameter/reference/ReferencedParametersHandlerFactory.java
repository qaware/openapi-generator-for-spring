package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

import java.util.List;

@RequiredArgsConstructor
public class ReferencedParametersHandlerFactory implements ReferencedItemHandlerFactory<List<Parameter>, List<Parameter>> {
    private final ReferenceDeciderForParameter referenceDecider;
    private final ReferenceIdentifierFactoryForParameter referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForParameter referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler<List<Parameter>, List<Parameter>> create() {
        ReferencedParameterStorage storage = new ReferencedParameterStorage(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
        return new ReferencedParametersHandlerImpl(storage, null);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forClassWithGenerics(List.class, Parameter.class);
    }
}
