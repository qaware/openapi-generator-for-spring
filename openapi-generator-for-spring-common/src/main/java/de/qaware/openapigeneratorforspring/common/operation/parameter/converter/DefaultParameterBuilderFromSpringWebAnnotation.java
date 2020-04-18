package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;


import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.web.bind.annotation.ValueConstants;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

public class DefaultParameterBuilderFromSpringWebAnnotation {

    Parameter build(ParameterIn in, String name, boolean required) {
        Parameter parameter = new Parameter()
                .in(in.toString())
                .required(required);
        setStringIfNotBlank(name, parameter::setName);
        return parameter;
    }

    Parameter build(ParameterIn in, String name, boolean required, String defaultValue) {
        return build(in, name, getRequiredFlag(required, defaultValue));
    }

    protected boolean getRequiredFlag(boolean required, String defaultValue) {
        if (!defaultValue.equals(ValueConstants.DEFAULT_NONE)) {
            return false;
        }
        return required;
    }
}
