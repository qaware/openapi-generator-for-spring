package de.qaware.openapigeneratorforspring.common.reference.fortype;

import io.swagger.v3.oas.models.Components;

public interface ReferencedItemHandler<T, U> extends ReferencedItemConsumerForType<T, U> {
    void applyToComponents(Components components);
}
