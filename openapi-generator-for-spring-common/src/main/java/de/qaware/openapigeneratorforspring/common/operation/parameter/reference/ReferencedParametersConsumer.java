package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.List;
import java.util.function.Consumer;

public interface ReferencedParametersConsumer {
    void maybeAsReferences(List<Parameter> parameters, Consumer<List<Parameter>> parametersSetter);
}
