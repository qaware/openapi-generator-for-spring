package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.HasAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HandlerMethod extends HasAnnotationsSupplier {
    String getIdentifier();

    List<Parameter> getParameters();

    interface Type extends HasAnnotationsSupplier {
        java.lang.reflect.Type getType();
    }

    interface HasType {
        Optional<Type> getType();
    }

    interface Parameter extends HasAnnotationsSupplier, HasType {
        Optional<String> getName();

        default void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter) {
            // do nothing, customization callback is optional
        }
    }

    interface RequestBody extends HasAnnotationsSupplier, HasType {

        Set<String> getConsumesContentTypes();

        default void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
            // do nothing, customization callback is optional
        }
    }

    interface Response extends HasType {
        String getResponseCode();

        Set<String> getProducesContentTypes();

        default void customize(ApiResponse apiResponse) {
            // do nothing, customization callback is optional
        }
    }

    @FunctionalInterface
    @Order()
    interface OperationAnnotationApplier {
        void applyTo(HandlerMethod handlerMethod, Operation operation);
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
    interface Merger {
        @Nullable
        HandlerMethod merge(List<HandlerMethod> handlerMethods);
    }
}
