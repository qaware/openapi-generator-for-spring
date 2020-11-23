package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;

@ToString
public class SpringWebHandlerMethod extends AbstractSpringWebHandlerMethod {

    @Getter
    private final Method method;

    public SpringWebHandlerMethod(AnnotationsSupplier annotationsSupplier, List<Parameter> parameters, Method method) {
        super(annotationsSupplier, parameters);
        this.method = method;
    }

    @Override
    public String getIdentifier() {
        return method.getName();
    }
}
