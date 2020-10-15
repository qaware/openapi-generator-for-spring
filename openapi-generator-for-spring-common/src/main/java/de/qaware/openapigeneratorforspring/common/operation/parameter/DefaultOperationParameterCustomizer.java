package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizer;
import de.qaware.openapigeneratorforspring.common.reference.component.parameter.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

@RequiredArgsConstructor
@Slf4j
public class DefaultOperationParameterCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final List<OperationParameterFilter> parameterFilters;
    private final List<ParameterMethodConverter> parameterMethodConverters;
    private final List<OperationParameterCustomizer> parameterCustomizers;
    private final ParameterAnnotationMapper parameterAnnotationMapper;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        List<Parameter> parameters = buildParameters(operationBuilderContext, Arrays.stream(operationAnnotation.parameters()));
        setParametersToOperation(operation, parameters, operationBuilderContext);
    }

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        List<Parameter> parameters = buildParameters(operationBuilderContext, Stream.empty());
        setParametersToOperation(operation, parameters, operationBuilderContext);
    }

    private List<Parameter> buildParameters(OperationBuilderContext operationBuilderContext, Stream<io.swagger.v3.oas.annotations.Parameter> additionalParameterAnnotations) {
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        AnnotationsSupplier methodAnnotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(method);

        Map<String, io.swagger.v3.oas.annotations.Parameter> parameterAnnotationMap = Stream.concat(
                additionalParameterAnnotations,
                methodAnnotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.Parameter.class)
        )
                .collect(Collectors.toMap(
                        io.swagger.v3.oas.annotations.Parameter::name,
                        x -> x
                ));

        List<Parameter> parameters = new ArrayList<>();

        // first start with the parameters from the handler method, keep the order!
        for (Parameter parameterFromMethod : getParametersFromHandlerMethod(operationBuilderContext)) {
            io.swagger.v3.oas.annotations.Parameter parameterAnnotation = parameterAnnotationMap.remove(parameterFromMethod.getName());
            if (parameterAnnotation != null) {
                parameterAnnotationMapper.applyFromAnnotation(parameterFromMethod, parameterAnnotation, operationBuilderContext.getReferencedItemConsumerSupplier());
            }
            parameters.add(parameterFromMethod);
        }

        // add leftover parameters from annotation only afterwards
        for (io.swagger.v3.oas.annotations.Parameter parameterAnnotation : parameterAnnotationMap.values()) {
            Parameter parameter = parameterAnnotationMapper.buildFromAnnotation(parameterAnnotation, operationBuilderContext.getReferencedItemConsumerSupplier());
            if (parameter != null) {
                parameters.add(parameter);
            }
        }
        return parameters;
    }

    private void setParametersToOperation(Operation operation, List<Parameter> parameters, OperationBuilderContext operationBuilderContext) {
        List<Parameter> filteredParameters = parameters.stream()
                .filter(parameter -> parameterFilters.stream().allMatch(filter -> filter.postAccept(parameter)))
                .collect(Collectors.toList());
        ReferencedParametersConsumer referencedParametersConsumer = operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedParametersConsumer.class);
        setCollectionIfNotEmpty(filteredParameters, p -> referencedParametersConsumer.withOwner(operation).maybeAsReference(p, operation::setParameters));
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
        if (!parameterFilters.stream().allMatch(filter -> filter.preAccept(methodParameter, parameterAnnotationsSupplier))) {
            return null;
        }
        return parameterMethodConverters.stream()
                .map(converter -> converter.convert(methodParameter, parameterAnnotationsSupplier))
                .filter(Objects::nonNull)
                .findFirst() // converters are ordered and the first one not returning null will be used
                .map(parameter -> {
                    // apply customizers after conversion
                    parameterCustomizers.forEach(customizer ->
                            customizer.customize(parameter, methodParameter, parameterAnnotationsSupplier, operationBuilderContext)
                    );
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
