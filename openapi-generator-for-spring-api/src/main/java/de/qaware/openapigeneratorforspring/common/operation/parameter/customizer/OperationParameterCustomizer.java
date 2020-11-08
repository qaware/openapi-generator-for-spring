package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import org.springframework.core.Ordered;


@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
@FunctionalInterface
public interface OperationParameterCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    void customize(Parameter parameter, OperationParameterCustomizerContext context);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
