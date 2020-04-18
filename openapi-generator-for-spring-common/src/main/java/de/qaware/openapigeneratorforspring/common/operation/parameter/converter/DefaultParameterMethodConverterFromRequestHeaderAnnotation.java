package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.web.bind.annotation.RequestHeader;

public class DefaultParameterMethodConverterFromRequestHeaderAnnotation extends ParameterMethodConverterFromAnnotation<RequestHeader> {

    public static final int ORDER = DEFAULT_ORDER;

    private final DefaultParameterBuilderFromSpringWebAnnotation parameterBuilder;

    public DefaultParameterMethodConverterFromRequestHeaderAnnotation(DefaultParameterBuilderFromSpringWebAnnotation parameterBuilder) {
        super(RequestHeader.class);
        this.parameterBuilder = parameterBuilder;
    }

    @Override
    protected Parameter buildParameter(RequestHeader annotation) {
        return parameterBuilder.build(ParameterIn.HEADER,
                annotation.name(), annotation.required(), annotation.defaultValue()
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
