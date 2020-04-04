package de.qaware.openapigeneratorforspring.common.operation;

import io.swagger.v3.oas.models.Operation;
import lombok.Value;

@Value(staticConstructor = "of")
public class OperationWithInfo {
    Operation operation;
    OperationInfo info;
}
