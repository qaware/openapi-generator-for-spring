package de.qaware.openapigeneratorforspring.test.app32;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Configuration
class App32Configuration {

    @Bean
    public OperationCustomizer operationCustomizerForExceptions(SchemaResolver schemaResolver, AnnotationsSupplierFactory annotationsSupplierFactory, ApplicationContext applicationContext) {
        List<Pair<ControllerAdviceBean, ExceptionHandlerMethodResolver>> exceptionHandlerAdviceCache = ControllerAdviceBean.findAnnotatedBeans(applicationContext).stream()
                .filter(bean -> bean.getBeanType() != null)
                .map(bean -> Pair.of(bean, new ExceptionHandlerMethodResolver(bean.getBeanType())))
                .filter(pair -> pair.getRight().hasExceptionMappings())
                .toList();

        return new OperationCustomizer() {
            @Override
            public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
                HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();
                if (handlerMethod instanceof SpringWebHandlerMethod) {
                    SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) handlerMethod;
                    Stream.of(springWebHandlerMethod.getMethod().getMethod().getExceptionTypes())
                            .map(this::uncheckedCast)
                            .forEach(exceptionType -> findExceptionHandlerMethod(springWebHandlerMethod.getMethod().getBeanType(), exceptionType).ifPresent(method -> {
                                AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(method);
                                String responseStatusCode = findResponseStatusCode(annotationsSupplier);

                                Content content = new Content();
                                MediaType mediaType = new MediaType();
                                schemaResolver.resolveFromType(SchemaResolver.Caller.of(SchemaResolver.Mode.FOR_SERIALIZATION),
                                        method.getGenericReturnType(), annotationsSupplier,
                                        operationBuilderContext.getReferencedItemConsumer(ReferencedSchemaConsumer.class),
                                        mediaType::setSchema
                                );
                                content.put(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);
                                ApiResponses apiResponses = Optional.ofNullable(operation.getResponses()).orElseGet(ApiResponses::new);
                                apiResponses.put(responseStatusCode, ApiResponse.builder().content(content).build());
                                operation.setResponses(apiResponses);
                            }));
                }
            }

            public String findResponseStatusCode(AnnotationsSupplier annotationsSupplier) {
                return annotationsSupplier
                        .findAnnotations(ResponseStatus.class)
                        .findFirst()
                        .map(ResponseStatus::code)
                        .map(HttpStatus::value)
                        .map(Object::toString)
                        .orElse("500");
            }

            private Optional<Method> findExceptionHandlerMethod(Class<?> handlerBeanType, Class<? extends Throwable> exceptionType) {
                Method method = new ExceptionHandlerMethodResolver(handlerBeanType).resolveMethodByExceptionType(exceptionType);
                if (method != null) {
                    return Optional.of(method);
                }
                return exceptionHandlerAdviceCache.stream()
                        .filter(p -> p.getLeft().isApplicableToBeanType(handlerBeanType))
                        .map(p -> p.getRight().resolveMethodByExceptionType(exceptionType))
                        .filter(Objects::nonNull)
                        .findFirst();
            }

            @SuppressWarnings("unchecked")
            private Class<? extends Throwable> uncheckedCast(Class<?> type) {
                // there's no way around it, see https://github.com/spring-projects/spring-framework/pull/26206
                return (Class<? extends Throwable>) type;
            }
        };
    }


}
