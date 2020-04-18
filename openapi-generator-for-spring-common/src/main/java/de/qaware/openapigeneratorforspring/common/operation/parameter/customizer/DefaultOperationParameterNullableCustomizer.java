package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Slf4j
public class DefaultOperationParameterNullableCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(Parameter parameter, AnnotationsSupplier parameterAnnotationsSupplier) {
        // TODO support more @Nullable / @NotNull annotations? combine with other places where @Nullable is checked?
        if (parameterAnnotationsSupplier.findFirstAnnotation(Nullable.class) != null) {
            Boolean required = parameter.getRequired();
            if (required != null && required) {
                LOGGER.warn("Method parameter {} marked as required but annotated as nullable. Ignoring annotation.", parameter);
            } else {
                parameter.setRequired(false);
            }
        }
    }
}
