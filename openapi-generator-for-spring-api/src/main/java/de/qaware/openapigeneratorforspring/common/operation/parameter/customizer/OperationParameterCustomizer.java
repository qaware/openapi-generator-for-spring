package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;


@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
@FunctionalInterface
public interface OperationParameterCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    void customize(Parameter parameter, @Nullable HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
