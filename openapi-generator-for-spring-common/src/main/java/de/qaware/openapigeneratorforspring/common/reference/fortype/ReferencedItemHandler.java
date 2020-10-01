package de.qaware.openapigeneratorforspring.common.reference.fortype;


import de.qaware.openapigeneratorforspring.model.Components;

public interface ReferencedItemHandler<T, U> extends ReferencedItemConsumerForType<T, U> {
    void applyToComponents(Components components);
}
