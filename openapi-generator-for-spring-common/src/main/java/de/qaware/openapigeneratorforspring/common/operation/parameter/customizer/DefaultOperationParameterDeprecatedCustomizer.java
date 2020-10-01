package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

public class DefaultOperationParameterDeprecatedCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(Parameter parameter, AnnotationsSupplier parameterAnnotationsSupplier) {
        // TODO combine with other places where @Deprecated is checked?
        if (parameterAnnotationsSupplier.findFirstAnnotation(Deprecated.class) != null) {
            parameter.setDeprecated(true);
        }
    }
}
