package de.qaware.openapigeneratorforspring.common.filter.operation.parameter;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

public interface OperationParameterFilter {
    default boolean preAccept(java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier) {
        return true;
    }

    default boolean postAccept(Parameter parameter) {
        return true;
    }
}
