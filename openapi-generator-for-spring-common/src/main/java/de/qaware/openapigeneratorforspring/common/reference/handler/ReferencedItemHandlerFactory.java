package de.qaware.openapigeneratorforspring.common.reference.handler;

import org.springframework.core.ResolvableType;

public interface ReferencedItemHandlerFactory<T, U> {
    ReferencedItemHandler<T, U> create();

    ResolvableType getResolvableTypeOfItem();
}
