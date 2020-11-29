package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.HasAnnotationsSupplier;

public class DefaultOperationParameterDeprecatedCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter, OperationParameterCustomizerContext context) {
        // TODO combine with other places where @Deprecated is checked?
        context.getHandlerMethodParameter()
                .map(HasAnnotationsSupplier::getAnnotationsSupplier)
                .flatMap(annotationsSupplier -> annotationsSupplier.findAnnotations(Deprecated.class).findFirst())
                .ifPresent(ignored -> parameter.setDeprecated(true));
    }
}
