package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import org.springframework.core.Ordered;

/**
 * Customizer for {@link Operation}. Run AFTER the {@link
 * io.swagger.v3.oas.annotations.Operation operation annotation} was applied.
 *
 * <p>{@link Ordered} can be used to resolve of conflicting customizations.
 */
@FunctionalInterface
public interface OperationCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize the given operation by reference.
     *
     * @param operation               operation
     * @param operationBuilderContext context for operation building
     */
    void customize(Operation operation, OperationBuilderContext operationBuilderContext);
}
