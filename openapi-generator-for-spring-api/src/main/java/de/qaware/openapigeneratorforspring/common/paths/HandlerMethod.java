package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;

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

    interface ReturnType {
        Type getType();

        AnnotationsSupplier getAnnotationsSupplier();
    }
}
