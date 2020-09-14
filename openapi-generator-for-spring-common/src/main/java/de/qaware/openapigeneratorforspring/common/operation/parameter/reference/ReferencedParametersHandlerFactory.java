package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandlerFactory;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

import java.util.List;

@RequiredArgsConstructor
public class ReferencedParametersHandlerFactory implements ReferencedItemHandlerFactory<List<Parameter>> {
    private final ReferenceDeciderForParameter referenceDecider;
    private final ReferenceNameFactoryForParameter referenceNameFactory;
    private final ReferenceNameConflictResolverForParameter referenceNameConflictResolver;

    @Override
    public ReferencedItemHandler<List<Parameter>> create() {
        ReferencedParameterStorage storage = new ReferencedParameterStorage(referenceDecider, referenceNameFactory, referenceNameConflictResolver);
        return new ReferencedParametersHandlerImpl(storage);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forClassWithGenerics(List.class, Parameter.class);
    }
}
