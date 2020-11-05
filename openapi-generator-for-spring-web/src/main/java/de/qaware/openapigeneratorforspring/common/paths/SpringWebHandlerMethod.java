package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SpringWebHandlerMethod implements HandlerMethod {

    @Getter(AccessLevel.PACKAGE)
    private final Method method;
    @Getter
    private final AnnotationsSupplier annotationsSupplier;
    @Getter
    private final List<Parameter> parameters;

    SpringWebHandlerMethod(Method method, AnnotationsSupplierFactory annotationsSupplierFactory) {
        this.method = method;
        this.annotationsSupplier = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method);
        this.parameters = Stream.of(method.getParameters())
                .map(parameter -> new SpringWebHandlerMethod.SpringWebParameter(this, parameter,
                        annotationsSupplierFactory.createFromAnnotatedElement(parameter),
                        annotationsSupplierFactory.createFromAnnotatedElement(parameter.getType())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getIdentifier() {
        return method.getName();
    }

    @Getter
    static class SpringWebParameter implements Parameter {

        public SpringWebParameter(SpringWebHandlerMethod parentMethod, java.lang.reflect.Parameter parameter,
                                  AnnotationsSupplier annotationsSupplier, AnnotationsSupplier annotationsSupplierForType) {
            this.parentMethod = parentMethod;
            this.parameter = parameter;
            this.annotationsSupplier = annotationsSupplier;
            this.annotationsSupplierForType = annotationsSupplierForType;
        }

        @Getter(AccessLevel.PACKAGE)
        private final SpringWebHandlerMethod parentMethod;
        @Getter(AccessLevel.PACKAGE)
        private final java.lang.reflect.Parameter parameter;
        private final AnnotationsSupplier annotationsSupplier;
        private final AnnotationsSupplier annotationsSupplierForType;

        @Override
        public String getName() {
            return parameter.getName();
        }
    }

    @RequiredArgsConstructor
    static class SpringWebRequestBodyParameter implements RequestBodyParameter {

        private final AnnotationsSupplier handlerMethodAnnotationsSupplier;
        private final org.springframework.web.bind.annotation.RequestBody springRequestBodyAnnotation;

        @Override
        public List<String> getConsumesContentTypes() {
            // TODO check if that logic here correctly mimics the way Spring is treating the "consumes" property
            // Spring uses it to conditionally check if that handler method is supposed to accept that request,
            // and we need an information on what is supposed to be sent from the client for that method
            return handlerMethodAnnotationsSupplier
                    .findAnnotations(RequestMapping.class)
                    .filter(requestMappingAnnotation -> !StringUtils.isAllBlank(requestMappingAnnotation.consumes()))
                    .findFirst()
                    .map(requestMappingAnnotation -> Arrays.asList(requestMappingAnnotation.consumes()))
                    .orElse(Collections.singletonList(org.springframework.http.MediaType.ALL_VALUE));
        }

        @Override
        public void customize(RequestBody requestBody) {
            if (requestBody.getRequired() == null) {
                requestBody.setRequired(springRequestBodyAnnotation.required());
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    static class SpringWebReturnType implements ReturnType {
        private final Type type;
        private final AnnotationsSupplier annotationsSupplier;
        private final AnnotationsSupplier handlerMethodAnnotationsSupplier;

        @Override
        public List<String> getProducesContentTypes() {
            // TODO check if that logic here correctly mimics the way Spring is treating the "produces" property
            return handlerMethodAnnotationsSupplier
                    .findAnnotations(RequestMapping.class)
                    .filter(requestMappingAnnotation -> !StringUtils.isAllBlank(requestMappingAnnotation.produces()))
                    .findFirst()
                    .map(requestMappingAnnotation -> Arrays.asList(requestMappingAnnotation.produces()))
                    // fallback to "all value" if nothing has been specified
                    .orElse(Collections.singletonList(org.springframework.http.MediaType.ALL_VALUE));
        }
    }
}
