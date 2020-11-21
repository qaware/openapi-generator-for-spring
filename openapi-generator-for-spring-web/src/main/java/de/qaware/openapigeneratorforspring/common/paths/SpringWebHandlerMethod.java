package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString(onlyExplicitlyIncluded = true)
class SpringWebHandlerMethod extends AbstractSpringWebHandlerMethod {

    @Getter(AccessLevel.PACKAGE)
    @ToString.Include
    private final Method method;

    SpringWebHandlerMethod(Method method, AnnotationsSupplierFactory annotationsSupplierFactory) {
        super(
                annotationsSupplierFactory.createFromMethodWithDeclaringClass(method),
                Stream.of(method.getParameters())
                        .map(parameter -> SpringWebParameter.of(parameter, annotationsSupplierFactory))
                        .collect(Collectors.toList())
        );
        this.method = method;
    }

    @Override
    public String getIdentifier() {
        return method.getName();
    }

    @Override
    List<RequestBody> getRequestBodies() {
        List<String> consumesContentTypes = findConsumesContentTypes();
        return getParameters().stream().flatMap(parameter ->
                parameter.getAnnotationsSupplier().findAnnotations(org.springframework.web.bind.annotation.RequestBody.class)
                        .findFirst()
                        .map(springWebRequestBodyAnnotation -> buildCustomizedSpringWebRequestBody(consumesContentTypes, parameter, springWebRequestBodyAnnotation.required()))
                        .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
        ).collect(Collectors.toList());
    }

    private SpringWebRequestBody buildCustomizedSpringWebRequestBody(List<String> consumesContentTypes, Parameter parameter, boolean isRequired) {
        return new SpringWebRequestBody(
                parameter.getAnnotationsSupplier(),
                consumesContentTypes,
                parameter.getType()
        ) {
            @Override
            public void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
                if (requestBody.getRequired() == null) {
                    requestBody.setRequired(isRequired);
                }
            }
        };
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
