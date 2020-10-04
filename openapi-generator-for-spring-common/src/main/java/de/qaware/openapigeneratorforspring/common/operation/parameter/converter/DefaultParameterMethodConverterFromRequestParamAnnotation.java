package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;

public class DefaultParameterMethodConverterFromRequestParamAnnotation extends ParameterMethodConverterFromAnnotation<RequestParam> {

    public static final int ORDER = DEFAULT_ORDER;

    private final DefaultParameterBuilderFromSpringWebAnnotation parameterBuilder;

    public DefaultParameterMethodConverterFromRequestParamAnnotation(DefaultParameterBuilderFromSpringWebAnnotation parameterBuilder) {
        super(RequestParam.class);
        this.parameterBuilder = parameterBuilder;
    }

    @Nullable
    @Override
    protected Parameter buildParameter(RequestParam annotation) {
        return parameterBuilder.build(ParameterIn.QUERY,
                annotation.name(), annotation.required(), annotation.defaultValue()
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
