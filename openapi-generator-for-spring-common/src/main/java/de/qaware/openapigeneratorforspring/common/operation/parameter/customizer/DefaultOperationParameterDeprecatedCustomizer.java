package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import io.swagger.v3.oas.models.parameters.Parameter;

public class DefaultOperationParameterDeprecatedCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(Parameter parameter, AnnotationsSupplier parameterAnnotationsSupplier) {
        // TODO combine with other places where @Deprecated is checked?
        if (parameterAnnotationsSupplier.findFirstAnnotation(Deprecated.class) != null) {
            parameter.deprecated(true);
        }
    }
}
