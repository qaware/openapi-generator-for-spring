package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import javax.annotation.Nullable;

/**
 * Converter for {@link  HandlerMethod.Parameter handler method
 * parameter}. Only if such a converter exists, the handler
 * method parameter will become part of the operation parameters.
 */
@FunctionalInterface
public interface ParameterMethodConverter extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Convert the given {@link  HandlerMethod.Parameter handler
     * method parameter} into an {@link Parameter operation parameter}.
     *
     * @param handlerMethodParameter handler method parameter
     * @return parameter, or null if nothing could be converted
     */
    @Nullable
    Parameter convert(HandlerMethod.Parameter handlerMethodParameter);
}
