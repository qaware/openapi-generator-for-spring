package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationParameterCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        // TODO override/add additional parameters from operation annotation

    }

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        List<Parameter> parameters = new ArrayList<>();

        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        for (java.lang.reflect.Parameter methodParameter : method.getParameters()) {
            AnnotationsSupplier parameterAnnotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(methodParameter);
            Parameter parameter = new Parameter()
                    .name(methodParameter.getName())
                    .in(getInProperty(parameterAnnotationsSupplier));
            parameters.add(parameter);
        }

        setCollectionIfNotEmpty(parameters, operation::setParameters);
    }

    @Nullable
    private String getInProperty(AnnotationsSupplier parameterAnnotationsSupplier) {
        if (parameterAnnotationsSupplier.findFirstAnnotation(RequestHeader.class) != null) {
            return "header";
        } else if (parameterAnnotationsSupplier.findFirstAnnotation(RequestParam.class) != null) {
            return "query";
        } else if (parameterAnnotationsSupplier.findFirstAnnotation(PathVariable.class) != null) {
            return "path";
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
