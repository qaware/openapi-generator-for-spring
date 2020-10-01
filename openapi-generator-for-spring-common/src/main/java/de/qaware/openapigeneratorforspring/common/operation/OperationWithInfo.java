package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.Value;

@Value(staticConstructor = "of")
public class OperationWithInfo {
    Operation operation;
    OperationInfo info;
}
