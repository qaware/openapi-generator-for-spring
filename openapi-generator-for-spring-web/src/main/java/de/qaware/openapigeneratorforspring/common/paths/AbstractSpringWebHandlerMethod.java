package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractSpringWebHandlerMethod implements HandlerMethod {

    @Getter
    private final AnnotationsSupplier annotationsSupplier;
    @Getter
    private final List<Parameter> parameters;

    abstract List<HandlerMethod.RequestBody> getRequestBodies();

    abstract List<HandlerMethod.Response> getResponses();

    Set<String> findConsumesContentTypes() {
        return fromRequestMappingAnnotation(RequestMapping::consumes);
    }

    Set<String> findProducesContentTypes() {
        return fromRequestMappingAnnotation(RequestMapping::produces);
    }

    private Set<String> fromRequestMappingAnnotation(Function<RequestMapping, String[]> mapper) {
        return annotationsSupplier.findAnnotations(RequestMapping.class)
                .filter(annotation -> !StringUtils.isAllBlank(mapper.apply(annotation)))
                // Spring doc says the first one should win,
                // ie. annotation on class level is overridden by method level
                .findFirst()
                .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
                .map(mapper)
                .flatMap(Stream::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    static class SpringWebType implements HandlerMethod.Type {
        private final java.lang.reflect.Type type;
        @With
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
        private final boolean required;

        @Override
        public void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
            if (requestBody.getRequired() == null) {
                requestBody.setRequired(required);
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static class SpringWebResponse implements Response {
        private final String responseCode;
        private final Set<String> producesContentTypes;
        private final Optional<Type> type;
    }
}
