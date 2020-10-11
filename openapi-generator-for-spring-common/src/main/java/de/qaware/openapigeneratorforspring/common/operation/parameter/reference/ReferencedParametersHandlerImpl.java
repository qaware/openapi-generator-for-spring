package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedParametersHandlerImpl implements ReferencedItemHandler<List<Parameter>, List<Parameter>>, ReferencedParametersConsumer {
    private final ReferencedParameterStorage storage;

    @With
    @Nullable
    private final Object owner;

    @Override
    public void maybeAsReference(List<Parameter> parameters, Consumer<List<Parameter>> parametersSetter) {
        parametersSetter.accept(parameters);
        // we use the fact that we can modify each parameter via index if it's supposed to be changed to a reference
        // using an IntStream here makes i effectively final and thus can be captured in nested lambda
        IntStream.range(0, parameters.size()).forEach(i ->
                storage.storeMaybeReference(
                        parameters.get(i),
                        parameterReference -> parameters.set(i, parameterReference)
                )
        );
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setParameters);
    }
}
