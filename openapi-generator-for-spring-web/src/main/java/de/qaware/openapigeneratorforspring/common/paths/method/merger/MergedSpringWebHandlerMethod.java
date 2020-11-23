package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import lombok.Getter;

import java.util.List;

@Getter
public class MergedSpringWebHandlerMethod extends AbstractSpringWebHandlerMethod {
    private final String identifier;
    private final List<SpringWebHandlerMethod> handlerMethods;

    public MergedSpringWebHandlerMethod(AnnotationsSupplier annotationsSupplier, List<Parameter> parameters, String identifier, List<SpringWebHandlerMethod> handlerMethods, List<RequestBody> requestBodies, List<Response> responses) {
        super(annotationsSupplier, parameters);
        this.identifier = identifier;
        this.handlerMethods = handlerMethods;
    }
}
