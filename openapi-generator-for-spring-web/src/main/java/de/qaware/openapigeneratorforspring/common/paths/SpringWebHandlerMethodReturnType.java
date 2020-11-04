package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

@RequiredArgsConstructor
@Getter
public class SpringWebHandlerMethodReturnType implements HandlerMethod.ReturnType {
    private final Type type;
    private final AnnotationsSupplier annotationsSupplier;
}
