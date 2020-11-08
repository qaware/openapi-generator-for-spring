package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

public class DefaultOperationParameterDeprecatedCustomizer extends AbstractHandlerMethodParameterCustomizer {
    @Override
    public void customizeWithHandlerMethod(Parameter parameter, HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext) {
        // TODO combine with other places where @Deprecated is checked?
        if (handlerMethodParameter.getAnnotationsSupplier().findFirstAnnotation(Deprecated.class) != null) {
            parameter.setDeprecated(true);
        }
    }
}
