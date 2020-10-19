package de.qaware.openapigeneratorforspring.common.filter.operation.parameter;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

@FunctionalInterface
public interface OperationParameterPostFilter {
    boolean postAccept(Parameter parameter);
}
