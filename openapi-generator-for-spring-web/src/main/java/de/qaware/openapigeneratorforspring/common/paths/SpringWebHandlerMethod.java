package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@ToString(onlyExplicitlyIncluded = true)
class SpringWebHandlerMethod extends AbstractSpringWebHandlerMethod {

    @Getter(AccessLevel.PACKAGE)
    @ToString.Include
    private final Method method;

    SpringWebHandlerMethod(Method method, AnnotationsSupplierFactory annotationsSupplierFactory) {
        super(annotationsSupplierFactory.createFromMethodWithDeclaringClass(method), method.getParameters(), annotationsSupplierFactory);
        this.method = method;
    }

    @Override
    public String getIdentifier() {
        return method.getName();
    }

    @RequiredArgsConstructor
    static class SpringWebResponse implements Response {
        @Getter
        private final List<String> producesContentTypes;
        private final SpringWebType springWebType;

        @Override
        public Optional<Type> getType() {
            return Optional.of(springWebType);
        }
    }
}
