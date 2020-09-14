package de.qaware.openapigeneratorforspring.common.reference.fortype;

import io.swagger.v3.oas.models.Components;

public interface ReferencedItemHandler<T> extends ReferencedItemConsumerForType<T> {
    void applyToComponents(Components components);
}
