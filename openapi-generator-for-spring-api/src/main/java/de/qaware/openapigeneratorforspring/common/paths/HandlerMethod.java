package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public interface HandlerMethod {
    String getIdentifier();
    AnnotationsSupplier getAnnotationsSupplier();
    List<Parameter> getParameters();

    interface Parameter {
        String getName();
        Optional<Type> getType();
        AnnotationsSupplier getAnnotationsSupplier();
        // annotations from parameter type are useful for SchemaResolver
        AnnotationsSupplier getAnnotationsSupplierForType();
    }

    interface RequestBodyParameter extends Parameter {
        List<String> getConsumesContentTypes();

        default void customize(RequestBody requestBody) {
            // do nothing, customization callback is optional
        }
    }

    interface ReturnType {
        Type getType();

        List<String> getProducesContentTypes();

        AnnotationsSupplier getAnnotationsSupplier();
    }

    // TODO use this at least for Spring Web!
    @FunctionalInterface
    @Order(0)
    interface MediaTypesParameterMapper {
        List<String> map(Parameter parameter);
    }

    @FunctionalInterface
    @Order(0)
    interface RequestBodyParameterMapper {
        @Nullable
        RequestBodyParameter map(Parameter parameter);
    }

    @FunctionalInterface
    @Order(0)
    interface ReturnTypeMapper {
        @Nullable
        ReturnType map(HandlerMethod handlerMethod);
    }

    @FunctionalInterface
    @Order(0)
    interface ApiResponseCodeMapper {
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
