package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

public class DefaultOperationParameterDeprecatedCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter().ifPresent(handlerMethodParameter -> {
            // TODO combine with other places where @Deprecated is checked?
            if (handlerMethodParameter.getAnnotationsSupplier().findFirstAnnotation(Deprecated.class) != null) {
                parameter.setDeprecated(true);
            }
        });
    }
}
