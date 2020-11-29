package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

import java.util.Optional;

/**
 * Context for {@link OperationParameterCustomizer}.
 *
 * <p>Extends {@link OperationBuilderContext} with an optional
 * {@link HandlerMethod.Parameter handler method paramter}.
 */
public interface OperationParameterCustomizerContext extends OperationBuilderContext {
    /**
     * Optional handler method parameter from which the to
     * be customized Parameter was converted from.
     *
     * @return Optional handler method parameter
     */
    Optional<HandlerMethod.Parameter> getHandlerMethodParameter();
}
