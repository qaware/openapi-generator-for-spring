package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

public interface OperationParameterCustomizer {
    default void customize(Parameter parameter, AnnotationsSupplier parameterAnnotationsSupplier) {
        // do nothing
    }

    default void customize(Parameter parameter, java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier, OperationBuilderContext operationBuilderContext) {
        // fallback to simpler method
        customize(parameter, parameterAnnotationsSupplier);
    }
}
