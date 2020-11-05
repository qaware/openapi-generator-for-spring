package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.annotation.Nullable;

public class DefaultParameterMethodConverterFromRequestHeaderAnnotation extends ParameterMethodConverterFromAnnotation<RequestHeader> {

    public static final int ORDER = DEFAULT_ORDER;

    private final SpringWebOpenApiParameterBuilder parameterBuilder;

    public DefaultParameterMethodConverterFromRequestHeaderAnnotation(SpringWebOpenApiParameterBuilder parameterBuilder) {
        super(RequestHeader.class);
        this.parameterBuilder = parameterBuilder;
    }

    @Nullable
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
