package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.HasReferencedItemConsumer;

import javax.annotation.Nullable;

/**
 * Context for operation building. Carries operation info, supports referencing
 * items and provides mapper context for additional annotation analysis.
 */
public interface OperationBuilderContext extends HasReferencedItemConsumer {
    OperationInfo getOperationInfo();

    /**
     * Obtain the mapper context, requiring an optional handler method
     * context for {@link HandlerMethod.ContextModifierMapper} application.
     *
     * @param context handler method context (maybe null)
     * @return mapper context to be used for additional annotation mapping
     */
    MapperContext getMapperContext(@Nullable HandlerMethod.Context context);
}
