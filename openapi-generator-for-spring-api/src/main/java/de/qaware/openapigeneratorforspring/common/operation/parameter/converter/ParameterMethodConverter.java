package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
@FunctionalInterface
public interface ParameterMethodConverter extends Ordered {

    int DEFAULT_ORDER = 0;

    @Nullable
    Parameter convert(HandlerMethod.Parameter handlerMethodParameter);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
