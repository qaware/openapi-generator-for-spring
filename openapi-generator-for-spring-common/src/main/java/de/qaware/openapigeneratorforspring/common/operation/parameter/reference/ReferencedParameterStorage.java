package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.Map;
import java.util.function.Consumer;

public interface ReferencedParameterStorage {
    void storeParameterMaybeReference(Parameter parameter, Consumer<ReferenceName> referenceNameConsumer);

    Map<String, Parameter> buildReferencedParameters();
}
