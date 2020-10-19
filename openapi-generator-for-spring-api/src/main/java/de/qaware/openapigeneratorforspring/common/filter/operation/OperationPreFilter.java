package de.qaware.openapigeneratorforspring.common.filter.operation;

import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;

@FunctionalInterface
public interface OperationPreFilter {
    boolean preAccept(OperationInfo operationInfo);
}
