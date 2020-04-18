package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface ParameterMethodConverter extends Ordered {

    int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    @Nullable
    Parameter convert(java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier);
}
