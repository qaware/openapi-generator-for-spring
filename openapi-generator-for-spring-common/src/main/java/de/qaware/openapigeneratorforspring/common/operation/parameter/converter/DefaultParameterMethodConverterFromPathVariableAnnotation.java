package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Nullable;

public class DefaultParameterMethodConverterFromPathVariableAnnotation extends ParameterMethodConverterFromAnnotation<PathVariable> {

    public static final int ORDER = DEFAULT_ORDER;

    private final DefaultParameterBuilderFromSpringWebAnnotation parameterBuilder;

    public DefaultParameterMethodConverterFromPathVariableAnnotation(DefaultParameterBuilderFromSpringWebAnnotation parameterBuilder) {
        super(PathVariable.class);
        this.parameterBuilder = parameterBuilder;
    }

    @Nullable
    @Override
    protected Parameter buildParameter(PathVariable annotation) {
        return parameterBuilder.build(ParameterIn.PATH,
                annotation.name(), annotation.required()
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
