package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.Value;

/**
 * {@link Operation} with {@link OperationInfo info}.
 */
@Value(staticConstructor = "of")
public class OperationWithInfo {
    Operation operation;
    OperationInfo info;
}
