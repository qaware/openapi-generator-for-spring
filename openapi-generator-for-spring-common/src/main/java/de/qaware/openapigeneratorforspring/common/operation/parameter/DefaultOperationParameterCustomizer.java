package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPreFilter;
import de.qaware.openapigeneratorforspring.common.mapper.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.parameter.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
@Slf4j
public class DefaultOperationParameterCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final List<OperationParameterPreFilter> parameterPreFilters;
    private final List<OperationParameterPostFilter> parameterPostFilters;
    private final List<ParameterMethodConverter> parameterMethodConverters;
    private final List<OperationParameterCustomizer> parameterCustomizers;
    private final ParameterAnnotationMapper parameterAnnotationMapper;

    @Override
    public void customize(Operation operation, @Nullable io.swagger.v3.oas.annotations.Operation operationAnnotation, OperationBuilderContext operationBuilderContext) {
        List<Parameter> parameters = buildParameters(operationBuilderContext, operationAnnotation);
        setParametersToOperation(operation, parameters, operationBuilderContext);
    }

    private List<Parameter> buildParameters(OperationBuilderContext operationBuilderContext, @Nullable io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();

        Map<String, io.swagger.v3.oas.annotations.Parameter> parameterAnnotationsByName = OpenApiMapUtils.buildStringMapFromStream(Stream.concat(
                Optional.ofNullable(operationAnnotation).map(io.swagger.v3.oas.annotations.Operation::parameters).map(Arrays::stream).orElse(Stream.empty()),
                handlerMethod.getAnnotationsSupplier().findAnnotations(io.swagger.v3.oas.annotations.Parameter.class)
        ), io.swagger.v3.oas.annotations.Parameter::name, x -> x);

        List<Parameter> parameters = new ArrayList<>();

        // first start with the parameters from the handler method, keep the order!
        for (Parameter parameterFromMethod : getParametersFromHandlerMethod(operationBuilderContext)) {
            io.swagger.v3.oas.annotations.Parameter parameterAnnotation = parameterAnnotationsByName.remove(parameterFromMethod.getName());
            if (parameterAnnotation != null) {
                parameterAnnotationMapper.applyFromAnnotation(parameterFromMethod, parameterAnnotation, operationBuilderContext.getMapperContext());
            }
            parameters.add(parameterFromMethod);
        }

        // add leftover parameters from annotation only afterwards
        for (io.swagger.v3.oas.annotations.Parameter parameterAnnotation : parameterAnnotationsByName.values()) {
            setIfNotEmpty(parameterAnnotationMapper.buildFromAnnotation(parameterAnnotation, operationBuilderContext.getMapperContext()),
                    parameters::add
            );
        }
        return parameters;
    }

    private void setParametersToOperation(Operation operation, List<Parameter> parameters, OperationBuilderContext operationBuilderContext) {
        List<Parameter> filteredParameters = parameters.stream()
                .filter(parameter -> parameterPostFilters.stream().allMatch(filter -> filter.postAccept(parameter)))
                .collect(Collectors.toList());
        ReferencedParametersConsumer referencedParametersConsumer = operationBuilderContext.getMapperContext().getReferenceConsumer(ReferencedParametersConsumer.class);
        setCollectionIfNotEmpty(filteredParameters, p -> referencedParametersConsumer.withOwner(operation).maybeAsReference(p, operation::setParameters));
    }

    private List<Parameter> getParametersFromHandlerMethod(OperationBuilderContext operationBuilderContext) {
        return operationBuilderContext.getOperationInfo().getHandlerMethod().getParameters().stream()
                .map(methodParameter -> convertFromMethodParameter(methodParameter, operationBuilderContext))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    private Parameter convertFromMethodParameter(HandlerMethod.Parameter methodParameter, OperationBuilderContext operationBuilderContext) {
        if (!parameterPreFilters.stream().allMatch(filter -> filter.preAccept(methodParameter))) {
            return null;
        }
        return parameterMethodConverters.stream()
                .map(converter -> converter.convert(methodParameter))
                .filter(Objects::nonNull)
                .findFirst() // converters are ordered and the first one not returning null will be used
                .map(parameter -> {
                    // apply customizers after conversion
                    parameterCustomizers.forEach(customizer ->
                            customizer.customize(parameter, methodParameter, operationBuilderContext)
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
