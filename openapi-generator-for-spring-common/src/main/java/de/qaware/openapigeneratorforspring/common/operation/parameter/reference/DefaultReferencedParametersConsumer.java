package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class DefaultReferencedParametersConsumer implements ReferencedParametersConsumer {
    private final ReferencedParameterStorage referencedParameterStorage;

    @Override
    public void maybeAsReferences(List<Parameter> parameters, Consumer<List<Parameter>> parametersSetter) {
        parametersSetter.accept(parameters);
        // we use the fact that we can modify each parameter via index if it's supposed to be changed to a reference
        // using an IntStream here makes i effectively final and thus can be captured in nested lambda
        IntStream.range(0, parameters.size()).forEach(i ->
                referencedParameterStorage.storeParameterMaybeReference(
                        parameters.get(i),
                        referenceName -> parameters.set(i, new Parameter().$ref(referenceName.asReferenceString()))
                )
        );
    }
}
