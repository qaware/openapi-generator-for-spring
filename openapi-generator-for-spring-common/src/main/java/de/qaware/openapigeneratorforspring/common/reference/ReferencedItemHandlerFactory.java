package de.qaware.openapigeneratorforspring.common.reference;

import org.springframework.core.ResolvableType;

public interface ReferencedItemHandlerFactory<T> {
    ReferencedItemHandler<T> create();

    ResolvableType getResolvableTypeOfItem();
}
