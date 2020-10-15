package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedParametersHandlerImpl implements ReferencedComponentHandler, ReferencedParametersConsumer {
    private final ReferencedParameterStorage storage;

    @With
    @Nullable
    private final Object owner;

    @Override
    public void maybeAsReference(List<Parameter> parameters, Consumer<List<Parameter>> parametersSetter) {
        parametersSetter.accept(parameters);
        storage.maybeReferenceParameters(parameters, owner);
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setParameters);
    }
}
