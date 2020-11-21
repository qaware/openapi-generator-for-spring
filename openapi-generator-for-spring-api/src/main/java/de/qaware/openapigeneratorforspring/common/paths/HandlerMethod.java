package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface HandlerMethod {
    String getIdentifier();

    AnnotationsSupplier getAnnotationsSupplier();

    List<Parameter> getParameters();

    interface Type {
        java.lang.reflect.Type getType();

        AnnotationsSupplier getAnnotationsSupplier();
    }

    interface Parameter {
        String getName();

        Optional<Type> getType();

        AnnotationsSupplier getAnnotationsSupplier();

        default void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter) {
            // do nothing, customization callback is optional
        }
    }

    interface RequestBody {
        Optional<Type> getType();

        List<String> getConsumesContentTypes();

        AnnotationsSupplier getAnnotationsSupplier();

        default void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
            // do nothing, customization callback is optional
        }
    }

    interface Response {
        Optional<Type> getType();

        List<String> getProducesContentTypes();
    }

    // TODO use this at least for Spring Web!
    @FunctionalInterface
    @Order(0)
    interface MediaTypesParameterMapper {
        @Nullable
        List<String> map(Parameter parameter);
    }

    @FunctionalInterface
    @Order(0)
    interface RequestBodyMapper {
        @Nullable
        List<RequestBody> map(HandlerMethod handlerMethod);
    }

    @FunctionalInterface
    @Order(0)
    interface ResponseMapper {
        @Nullable
        List<Response> map(HandlerMethod handlerMethod);
    }

    @FunctionalInterface
    @Order(0)
    interface ResponseCodeMapper {
        @Nullable
        String map(HandlerMethod handlerMethod);
    }

    @FunctionalInterface
    @Order(0)
    interface Merger {
        @Nullable
        HandlerMethod merge(List<HandlerMethod> handlerMethods);
    }
}
