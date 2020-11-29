package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import org.springframework.core.Ordered;


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
public interface OperationParameterCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    void customize(Parameter parameter, OperationParameterCustomizerContext context);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
