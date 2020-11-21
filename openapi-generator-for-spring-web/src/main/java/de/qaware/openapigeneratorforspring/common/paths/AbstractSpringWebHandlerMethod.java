package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractSpringWebHandlerMethod implements HandlerMethod {

    @Getter
    private final AnnotationsSupplier annotationsSupplier;
    @Getter
    private final List<Parameter> parameters;

    abstract List<HandlerMethod.RequestBody> getRequestBodies();

    protected Set<String> findConsumesContentTypes() {
        // TODO check if that logic here correctly mimics the way Spring is treating the "consumes" property
        // Spring uses it to conditionally check if that handler method is supposed to accept that request,
        // and we need an information on what is supposed to be sent from the client for that method
        return annotationsSupplier
                .findAnnotations(RequestMapping.class)
                .filter(requestMappingAnnotation -> !StringUtils.isAllBlank(requestMappingAnnotation.consumes()))
                .findFirst()
                .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
                .map(RequestMapping::consumes)
                .flatMap(Stream::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    static class SpringWebType implements HandlerMethod.Type {
        private final java.lang.reflect.Type type;
        private final AnnotationsSupplier annotationsSupplier;
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
    static class SpringWebParameter implements Parameter {

        static SpringWebParameter of(java.lang.reflect.Parameter parameter, AnnotationsSupplierFactory annotationsSupplierFactory) {
            return new SpringWebParameter(
                    // avoid building with some "arg1" auto-generated parameters
                    parameter.isNamePresent() ? parameter.getName() : null,
                    SpringWebType.of(parameter.getParameterizedType(), annotationsSupplierFactory.createFromAnnotatedElement(parameter.getType())),
                    annotationsSupplierFactory.createFromAnnotatedElement(parameter)
            );
        }

        @Nullable
        private final String parameterName;
        private final Type parameterType;
        @Getter
        private final AnnotationsSupplier annotationsSupplier;

        @Override
        public Optional<String> getName() {
            return Optional.ofNullable(parameterName);
        }

        @Override
        public Optional<Type> getType() {
            return Optional.of(parameterType);
        }
    }

    @RequiredArgsConstructor
    @Getter
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static class SpringWebRequestBody implements RequestBody {
        private final AnnotationsSupplier annotationsSupplier;
        private final Set<String> consumesContentTypes;
        private final Optional<Type> type;
    }

    @RequiredArgsConstructor
    static class SpringWebResponse implements Response {
        @Getter
        private final Set<String> producesContentTypes;
        private final SpringWebType springWebType;

        @Override
        public Optional<Type> getType() {
            return Optional.of(springWebType);
        }
    }
}
