package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultSpringWebHandlerMethodBuilder implements SpringWebHandlerMethodBuilder {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public HandlerMethod build(org.springframework.web.method.HandlerMethod springWebHandlerMethod) {
        return new SpringWebHandlerMethod(springWebHandlerMethod.getMethod(), annotationsSupplierFactory);
    }
}
