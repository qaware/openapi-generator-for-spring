package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Slf4j
public class DefaultOperationParameterNullableCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter().ifPresent(handlerMethodParameter -> {
            // TODO support more @Nullable / @NotNull annotations? combine with other places where @Nullable is checked?
            if (handlerMethodParameter.getAnnotationsSupplier().findFirstAnnotation(Nullable.class) != null) {
                Boolean required = parameter.getRequired();
                if (required != null && required) {
                    LOGGER.warn("Method parameter {} marked as required but annotated as nullable. Ignoring annotation.", parameter);
                } else {
                    // TODO is this always right to set it to false?
                    parameter.setRequired(false);
                }
            }
        });
    }
}
