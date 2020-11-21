package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractSpringWebHandlerMethod implements HandlerMethod {

    @Getter
    private final AnnotationsSupplier annotationsSupplier;
    @Getter
    private final List<Parameter> parameters;

    public AbstractSpringWebHandlerMethod(AnnotationsSupplier annotationsSupplier, java.lang.reflect.Parameter[] methodParameters, AnnotationsSupplierFactory annotationsSupplierFactory) {
        this.annotationsSupplier = annotationsSupplier;
        this.parameters = Stream.of(methodParameters)
                .map(parameter -> SpringWebParameter.of(parameter, annotationsSupplierFactory))
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    static class SpringWebType implements HandlerMethod.Type {
        private final java.lang.reflect.Type type;
        private final AnnotationsSupplier annotationsSupplier;
    }

    @RequiredArgsConstructor
    static class SpringWebParameter implements Parameter {

        static SpringWebParameter of(java.lang.reflect.Parameter parameter, AnnotationsSupplierFactory annotationsSupplierFactory) {
            AnnotationsSupplier annotationsSupplierForType = annotationsSupplierFactory.createFromAnnotatedElement(parameter.getType());
            return new SpringWebParameter(
                    parameter.getName(),
                    annotationsSupplierFactory.createFromAnnotatedElement(parameter),
                    SpringWebType.of(parameter.getParameterizedType(), annotationsSupplierForType)
            );
        }

        @Getter
        private final String name;
        @Getter
        private final AnnotationsSupplier annotationsSupplier;
        @Getter(AccessLevel.PACKAGE)
        private final SpringWebType springWebType;

        @Override
        public Optional<Type> getType() {
            return Optional.of(springWebType);
        }
    }

    @RequiredArgsConstructor
    static class SpringWebRequestBody implements RequestBody {
        @Getter
        private final AnnotationsSupplier annotationsSupplier;
        @Getter
        private final List<String> consumesContentTypes;
        private final boolean isRequired;
        private final SpringWebType springWebType;

        @Override
        public Optional<Type> getType() {
            return Optional.of(springWebType);
        }

        @Override
        public void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
            if (requestBody.getRequired() == null) {
                requestBody.setRequired(isRequired);
            }
        }
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
