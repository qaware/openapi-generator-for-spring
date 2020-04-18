package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import io.swagger.v3.oas.models.parameters.Parameter;

import javax.annotation.Nullable;

public interface ParameterAnnotationMapper {
    @Nullable
    Parameter buildFromAnnotation(io.swagger.v3.oas.annotations.Parameter parameterAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);

    void applyFromAnnotation(Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}
