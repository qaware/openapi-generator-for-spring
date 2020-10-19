package de.qaware.openapigeneratorforspring.common.filter.operation.parameter;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;

@FunctionalInterface
public interface OperationParameterPreFilter {
    boolean preAccept(java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier);
}
