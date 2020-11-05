package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@ToString(onlyExplicitlyIncluded = true)
class RouterFunctionHandlerMethod implements HandlerMethod {

    static final AnnotationsSupplier EMPTY_ANNOTATIONS_SUPPLIER = new AnnotationsSupplier() {
        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            return Stream.empty();
        }
    };

    @ToString.Include
    private final String identifier;
    @ToString.Include
    private final RouterFunction<?> routerFunction;
    private final AnnotationsSupplier annotationsSupplier;
    private final RouterFunctionAnalysis.Result routerFunctionAnalysisResult;

    @Override
    public List<HandlerMethod.Parameter> getParameters() {
        // TODO add dummy request body parameter here if content-type is not empty?
        return routerFunctionAnalysisResult.getParameters();
    }

    @RequiredArgsConstructor
    static class Parameter implements HandlerMethod.Parameter {
        @Getter
        private final String name;
        @Getter(AccessLevel.PACKAGE)
        private final ParameterIn parameterIn;

        @Override
        public AnnotationsSupplier getAnnotationsSupplier() {
            return EMPTY_ANNOTATIONS_SUPPLIER;
        }

        @Override
        public AnnotationsSupplier getAnnotationsSupplierForType() {
            return EMPTY_ANNOTATIONS_SUPPLIER;
        }
    }

    @RequiredArgsConstructor
    @Getter
    static class ReturnType implements HandlerMethod.ReturnType {

        private final List<String> producesContentTypes;

        @Override
        public Type getType() {
            // just a marker to get schema resolution working
            // with @Schema on the bean factory method
            return getClass();
        }

        @Override
        public AnnotationsSupplier getAnnotationsSupplier() {
            return EMPTY_ANNOTATIONS_SUPPLIER;
        }
    }
}
