package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;


/**
 * Customizer for {@link Parameter operation parameter}.
 *
 * <p>Is run AFTER the parameter has been {@link
 * de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter
 * converted}.
 *
 * <p>Note that there might be also parameters which are not
 * found from the handler method. Those are also customized,
 * but the {@link OperationParameterCustomizerContext} does
 * not supply a handler method parameter in this case.
 */
@FunctionalInterface
public interface OperationParameterCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize parameter by reference.
     *
     * @param parameter parameter
     * @param context   context for customization
     */
    void customize(Parameter parameter, OperationParameterCustomizerContext context);
}
