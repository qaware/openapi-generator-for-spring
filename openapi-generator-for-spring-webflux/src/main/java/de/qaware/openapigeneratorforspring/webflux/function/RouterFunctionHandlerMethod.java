package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(onlyExplicitlyIncluded = true)
class RouterFunctionHandlerMethod implements HandlerMethod {

    @ToString.Include
    @Getter
    private final String identifier;
    @Getter
    private final AnnotationsSupplier annotationsSupplier;
    @ToString.Include
    private final RouterFunction<?> routerFunction;
    @Getter(AccessLevel.PACKAGE)
    private final RouterFunctionAnalysis.Result routerFunctionAnalysisResult;

    @Override
    public List<HandlerMethod.Parameter> getParameters() {
        return routerFunctionAnalysisResult.getParameters();
    }

    @RequiredArgsConstructor
    @Getter
    static class RouterFunctionType implements HandlerMethod.Type {
        private final AnnotationsSupplier annotationsSupplier;
        private final java.lang.reflect.Type type;
    }

    @RequiredArgsConstructor
    static class Parameter implements HandlerMethod.Parameter {
        private final String routerParameterName;
        @Getter(AccessLevel.PACKAGE)
        private final ParameterIn parameterIn;

        @Override
        public Optional<String> getName() {
            return Optional.of(routerParameterName);
        }

        @Override
        public AnnotationsSupplier getAnnotationsSupplier() {
            return AnnotationsSupplier.EMPTY;
        }

        @Override
        public Optional<Type> getType() {
            return Optional.empty();
        }
    }

    @RequiredArgsConstructor
    @Getter
    static class Response implements HandlerMethod.Response {

        private final List<String> producesContentTypes;
        private final RouterFunctionType routerFunctionType;

        @Override
        public Optional<Type> getType() {
            return Optional.of(routerFunctionType);
        }
    }
}
