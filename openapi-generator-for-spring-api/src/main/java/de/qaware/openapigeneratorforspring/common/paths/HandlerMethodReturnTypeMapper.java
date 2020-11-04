package de.qaware.openapigeneratorforspring.common.paths;

import javax.annotation.Nullable;

@FunctionalInterface
public interface HandlerMethodReturnTypeMapper {
    @Nullable
    HandlerMethod.ReturnType map(HandlerMethod handlerMethod);
}
