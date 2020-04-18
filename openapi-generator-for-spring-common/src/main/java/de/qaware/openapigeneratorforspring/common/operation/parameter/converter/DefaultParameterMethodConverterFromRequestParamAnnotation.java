package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

public class DefaultParameterMethodConverterFromRequestParamAnnotation extends ParameterMethodConverterFromAnnotation<RequestParam> {

    public static final int ORDER = DEFAULT_ORDER;

    private final DefaultParameterBuilderFromSpringWebAnnotation parameterBuilder;

    public DefaultParameterMethodConverterFromRequestParamAnnotation(DefaultParameterBuilderFromSpringWebAnnotation parameterBuilder) {
        super(RequestParam.class);
        this.parameterBuilder = parameterBuilder;
    }

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
