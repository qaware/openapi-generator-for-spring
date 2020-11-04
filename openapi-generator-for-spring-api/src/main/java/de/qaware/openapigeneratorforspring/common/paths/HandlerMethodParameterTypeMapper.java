package de.qaware.openapigeneratorforspring.common.paths;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

@FunctionalInterface
public interface HandlerMethodParameterTypeMapper {
    @Nullable
    Type map(HandlerMethod.Parameter parameter);
}
