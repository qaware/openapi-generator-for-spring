package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import io.swagger.v3.oas.models.parameters.Parameter;

import javax.annotation.Nullable;

public interface ParameterAnnotationMapper {
    @Nullable
    Parameter buildFromAnnotation(io.swagger.v3.oas.annotations.Parameter parameterAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);

    void applyFromAnnotation(Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}
