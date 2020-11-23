package de.qaware.openapigeneratorforspring.common.paths.method.merger;


import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.stream.Stream;

public class SpringWebHandlerMethodTypeMerger {

    public <T extends HandlerMethod.HasType> Optional<HandlerMethod.Type> mergeTypes(Stream<T> stream) {
        return stream
                .map(HandlerMethod.HasType::getType)
                .map(type -> type.orElseThrow(() -> new IllegalStateException("Spring Web Handler Method Parameter should always have a type present")))
                .reduce(this::mergeType);
    }

    public HandlerMethod.Type mergeType(HandlerMethod.Type a, HandlerMethod.Type b) {
        if (isNotVoid(a) && isNotVoid(b) && !a.getType().equals(b.getType())) {
            throw new IllegalStateException("Cannot merge conflicting types: " + a + " vs. " + b);
        }
        return AbstractSpringWebHandlerMethod.SpringWebType.of(
                chooseParameterType(a, b),
                a.getAnnotationsSupplier().andThen(b.getAnnotationsSupplier())
        );
    }

    private static Type chooseParameterType(HandlerMethod.Type a, HandlerMethod.Type b) {
        return isNotVoid(a) ? a.getType() : b.getType();
    }

    private static boolean isNotVoid(HandlerMethod.Type type) {
        return !void.class.equals(type.getType()) && !Void.class.equals(type.getType());
    }
}
