package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

@RequiredArgsConstructor
@Getter
public class SpringWebHandlerMethodParameter implements HandlerMethod.Parameter {
    private final String name;
    private final AnnotationsSupplier annotationsSupplier;
    private final AnnotationsSupplier annotationsSupplierForType;
    private final Type type;
}
