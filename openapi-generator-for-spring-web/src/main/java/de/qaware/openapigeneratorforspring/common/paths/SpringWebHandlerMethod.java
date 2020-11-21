package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodMapper.ifEmptyUseSingleAllValue;

@ToString(onlyExplicitlyIncluded = true)
class SpringWebHandlerMethod extends AbstractSpringWebHandlerMethod {

    @Getter(AccessLevel.PACKAGE)
    @ToString.Include
    private final Method method;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    SpringWebHandlerMethod(Method method, AnnotationsSupplierFactory annotationsSupplierFactory) {
        super(
                annotationsSupplierFactory.createFromMethodWithDeclaringClass(method),
                Stream.of(method.getParameters())
                        .map(parameter -> SpringWebParameter.of(parameter, annotationsSupplierFactory))
                        .collect(Collectors.toList())
        );
        this.method = method;
        this.annotationsSupplierFactory = annotationsSupplierFactory;
    }

    @Override
    public String getIdentifier() {
        return method.getName();
    }

    @Override
    List<RequestBody> getRequestBodies() {
        Set<String> consumesContentTypes = findConsumesContentTypes();
        return getParameters().stream()
                .flatMap(parameter -> buildSpringWebRequestBodies(parameter, consumesContentTypes))
                .collect(Collectors.toList());
    }

    @Override
    List<Response> getResponses() {
        // using method.getReturnType() does not work for generic return types
        return Collections.singletonList(new SpringWebResponse(
                getResponseCode(),
                ifEmptyUseSingleAllValue(findProducesContentTypes()),
                Optional.of(getReturnType())
        ) {
            @Override
            public void customize(ApiResponse apiResponse) {
                Content content = apiResponse.getContent();
                if (content != null && content.size() == 1
                        && getProducesContentTypes().equals(content.keySet())
                        && content.values().stream().allMatch(HasIsEmpty::isEmpty)
                ) {
                    apiResponse.setContent(null);
                }
            }
        });
    }

    SpringWebType getReturnType() {
        // even for Void method return type, there might still be @Schema annotation which could be useful
        return SpringWebType.of(method.getGenericReturnType(), annotationsSupplierFactory.createFromAnnotatedElement(method.getReturnType()));
    }

    String getResponseCode() {
        return getAnnotationsSupplier().findAnnotations(ResponseStatus.class)
                .map(ResponseStatus::code)
                .mapToInt(HttpStatus::value)
                .mapToObj(Integer::toString)
                .findFirst()
                .orElseGet(() -> Integer.toString(HttpStatus.OK.value()));
    }

    static Stream<RequestBody> buildSpringWebRequestBodies(HandlerMethod.Parameter parameter, Set<String> consumesContentTypes) {
        return parameter.getAnnotationsSupplier().findAnnotations(org.springframework.web.bind.annotation.RequestBody.class)
                .map(org.springframework.web.bind.annotation.RequestBody::required)
                .reduce((a, b) -> a || b) // TODO maybe log warning that there's a conflict in required flag?
                .map(requiredFlag -> buildCustomizedSpringWebRequestBody(consumesContentTypes, parameter, requiredFlag))
                .map(Stream::of).orElseGet(Stream::empty); // Optional.toStream()
    }

    private static RequestBody buildCustomizedSpringWebRequestBody(Set<String> consumesContentTypes, Parameter parameter, boolean isRequired) {
        return new SpringWebRequestBody(
                parameter.getAnnotationsSupplier(),
                ifEmptyUseSingleAllValue(consumesContentTypes),
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
}
