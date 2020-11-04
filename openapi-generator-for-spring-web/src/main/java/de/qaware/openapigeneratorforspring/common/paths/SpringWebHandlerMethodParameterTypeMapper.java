package de.qaware.openapigeneratorforspring.common.paths;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public class SpringWebHandlerMethodParameterTypeMapper implements HandlerMethodParameterTypeMapper {
    @Nullable
    @Override
    public Type map(HandlerMethod.Parameter parameter) {
        if (parameter instanceof SpringWebHandlerMethodParameter) {
            return ((SpringWebHandlerMethodParameter) parameter).getType();
        }
        return null;
    }
}
