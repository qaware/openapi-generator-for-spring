package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

public class SpringWebHandlerMethodRequestBodyParameterMapper {

    public Optional<RequestBodyParameter> findRequestBodyParameter(SpringWebHandlerMethod handlerMethod) {
        return handlerMethod.getParameters().stream()
                .flatMap(parameter -> parameter.getAnnotationsSupplier()
                        .findAnnotations(org.springframework.web.bind.annotation.RequestBody.class)
                        .findFirst()
                        .map(org.springframework.web.bind.annotation.RequestBody::required)
                        .map(requiredFlag -> RequestBodyParameter.of(parameter, requiredFlag))
                        .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
                )
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one parameter marked with @RequestBody on " + this);
                });
    }

    @RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    @Getter
    public static class RequestBodyParameter {
        private final HandlerMethod.Parameter parameter;
        private final boolean required;
    }
}
