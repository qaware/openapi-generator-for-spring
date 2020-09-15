package de.qaware.openapigeneratorforspring.common.reference.fortype;

import org.springframework.core.ResolvableType;

public interface ReferencedItemHandlerFactory<T, U> {
    ReferencedItemHandler<T, U> create();

    ResolvableType getResolvableTypeOfItem();
}
