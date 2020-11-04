package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class SpringWebHandlerMethod implements HandlerMethod {
    private final String identifier;
    private final AnnotationsSupplier annotationsSupplier;
    private final Class<?> returnType;
    private final Type genericReturnType;
    private final List<Parameter> parameters;
}
