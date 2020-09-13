package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemHandlerFactory;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

import java.util.List;

@RequiredArgsConstructor
public class ReferencedParametersHandlerFactory implements ReferencedItemHandlerFactory<List<Parameter>> {
    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolverForParameter referenceNameConflictResolver;
    private final ReferenceDeciderForParameter referenceDecider;

    @Override
    public ReferencedItemHandler<List<Parameter>> create() {
        ReferencedParameterStorage storage = new ReferencedParameterStorage(referenceNameFactory, referenceNameConflictResolver, referenceDecider);
        return new ReferencedParametersHandlerImpl(storage);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forClassWithGenerics(List.class, Parameter.class);
    }
}
