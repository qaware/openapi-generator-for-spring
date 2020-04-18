package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizer;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;

@RequiredArgsConstructor
@Slf4j
public class DefaultOperationParameterCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final List<ParameterMethodConverter> parameterMethodConverters;
    private final List<OperationParameterCustomizer> operationParameterCustomizers;
    private final ParameterAnnotationMapper parameterAnnotationMapper;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {

        Map<String, io.swagger.v3.oas.annotations.Parameter> parameterAnnotationMap = buildMapFromArray(
                operationAnnotation.parameters(),
                io.swagger.v3.oas.annotations.Parameter::name,
                x -> x
        );

        List<Parameter> parameters = new ArrayList<>();

        // first start with the parameters from the handler method, keep the order!
        for (Parameter parameterFromMethod : getParametersFromHandlerMethod(operationBuilderContext)) {
            io.swagger.v3.oas.annotations.Parameter parameterAnnotation = parameterAnnotationMap.remove(parameterFromMethod.getName());
            if (parameterAnnotation != null) {
                parameterAnnotationMapper.applyFromAnnotation(parameterFromMethod, parameterAnnotation, operationBuilderContext.getReferencedSchemaConsumer());
            }
            parameters.add(parameterFromMethod);
        }

        // add leftover parameters from annotation only afterwards
        for (io.swagger.v3.oas.annotations.Parameter parameterAnnotation : parameterAnnotationMap.values()) {
            Parameter parameter = parameterAnnotationMapper.buildFromAnnotation(parameterAnnotation, operationBuilderContext.getReferencedSchemaConsumer());
            if (parameter != null) {
                parameters.add(parameter);
            }
        }

        setCollectionIfNotEmpty(parameters, operation::setParameters);
    }

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        List<Parameter> parameters = getParametersFromHandlerMethod(operationBuilderContext);
        setCollectionIfNotEmpty(parameters, operation::setParameters);
    }

    private List<Parameter> getParametersFromHandlerMethod(OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        return Stream.of(method.getParameters())
                .map(methodParameter -> convertFromMethodParameter(methodParameter, operationBuilderContext))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    private Parameter convertFromMethodParameter(java.lang.reflect.Parameter methodParameter, OperationBuilderContext operationBuilderContext) {
        AnnotationsSupplier parameterAnnotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(methodParameter);
        return parameterMethodConverters.stream()
                .map(converter -> converter.convert(methodParameter, parameterAnnotationsSupplier))
                .filter(Objects::nonNull)
                .findFirst() // converters are ordered and the first one not returning null will be used
                .map(parameter -> {
                    // apply customizers after conversion
                    operationParameterCustomizers.
                            forEach(customizer -> customizer.customize(parameter, methodParameter, parameterAnnotationsSupplier, operationBuilderContext));
                    return parameter;
                })
                // body method parameters are typical candidates which are ignored
                .orElse(null);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
