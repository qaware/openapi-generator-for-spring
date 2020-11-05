package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;

public interface HandlerMethod {
    String getIdentifier();
    AnnotationsSupplier getAnnotationsSupplier();

    List<Parameter> getParameters();

    interface Parameter {
        @Nullable
        String getName();

        AnnotationsSupplier getAnnotationsSupplier();

        // annotations from parameter type are useful for SchemaResolver
        AnnotationsSupplier getAnnotationsSupplierForType();
    }

    interface RequestBodyParameter {
        List<String> getConsumesContentTypes();

        void customize(RequestBody requestBody);
    }

    interface ReturnType {
        Type getType();

        List<String> getProducesContentTypes();

        AnnotationsSupplier getAnnotationsSupplier();
    }

    @FunctionalInterface
    @Order(0)
    interface ParameterTypeMapper {
        @Nullable
        Type map(Parameter parameter);
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
}
