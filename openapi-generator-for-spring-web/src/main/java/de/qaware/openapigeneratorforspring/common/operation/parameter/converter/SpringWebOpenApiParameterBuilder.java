package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;


import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.ValueConstants;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

public class SpringWebOpenApiParameterBuilder {

    Parameter build(ParameterIn in, String name, boolean requiredFlag) {
        Parameter parameter = Parameter.builder()
                .in(in.toString())
                .required(requiredFlag)
                .build();
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
