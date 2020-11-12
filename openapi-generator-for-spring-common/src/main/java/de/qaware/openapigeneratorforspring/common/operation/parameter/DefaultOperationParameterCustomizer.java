package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPreFilter;
import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizerContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.parameter.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.ensureKeyIsNotBlank;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
public class DefaultOperationParameterCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final List<OperationParameterPreFilter> parameterPreFilters;
    private final List<OperationParameterPostFilter> parameterPostFilters;
    private final List<ParameterMethodConverter> parameterMethodConverters;
    private final List<OperationParameterCustomizer> parameterCustomizers;
    private final OperationParameterCustomizerContextFactory operationParameterCustomizerContextFactory;
    private final ParameterAnnotationMapper parameterAnnotationMapper;

    @Override
    public void customize(Operation operation, @Nullable io.swagger.v3.oas.annotations.Operation operationAnnotation, OperationBuilderContext operationBuilderContext) {
        // this customizer explicitly overrides already present parameters built from the operation annotation,
        // as the order of parameters is important and should not be determined
        // by the order of information to build those parameters!
        // TODO maybe consider making the above choice about parameter ordering better bean-customizable?
        List<Parameter> parameters = buildParameters(operationBuilderContext, operationAnnotation);
        setParametersToOperation(operation, parameters, operationBuilderContext);
    }

    private List<Parameter> buildParameters(OperationBuilderContext operationBuilderContext, @Nullable io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();

        Map<String, List<io.swagger.v3.oas.annotations.Parameter>> parameterAnnotationsByName = Stream.concat(
                Optional.ofNullable(operationAnnotation).map(io.swagger.v3.oas.annotations.Operation::parameters).map(Arrays::stream).orElse(Stream.empty()),
                handlerMethod.getAnnotationsSupplier().findAnnotations(io.swagger.v3.oas.annotations.Parameter.class)
        ).collect(groupingBy(ensureKeyIsNotBlank(io.swagger.v3.oas.annotations.Parameter::name), LinkedHashMap::new, toList()));

        List<Parameter> parameters = new ArrayList<>();
        MapperContext mapperContext = operationBuilderContext.getMapperContext();

        // first start with the parameters from the handler method, keep the order!
        for (Parameter parameterFromMethod : getParametersFromHandlerMethod(operationBuilderContext)) {
            List<io.swagger.v3.oas.annotations.Parameter> parameterAnnotations = parameterAnnotationsByName.remove(parameterFromMethod.getName());
            if (parameterAnnotations != null) {
                parameterAnnotations.forEach(parameterAnnotation -> parameterAnnotationMapper.applyFromAnnotation(parameterFromMethod, parameterAnnotation, mapperContext));
            }
            parameters.add(parameterFromMethod);
        }

        // add leftover parameters from annotation only afterwards
        parameterAnnotationsByName.forEach((parameterName, parameterAnnotations) -> {
            // there must be at least one parameter annotation, as the map is a result of a groupingBy collector
            Iterator<io.swagger.v3.oas.annotations.Parameter> iterator = parameterAnnotations.iterator();
            Parameter parameter = parameterAnnotationMapper.buildFromAnnotation(iterator.next(), mapperContext);
            iterator.forEachRemaining(parameterAnnotation -> parameterAnnotationMapper.applyFromAnnotation(parameter, parameterAnnotation, mapperContext));
            // also customizers should be applied to parameters not coming from method parameters
            applyParameterCustomizers(parameter, operationParameterCustomizerContextFactory.create(operationBuilderContext, null));
            setIfNotEmpty(parameter, parameters::add);
        });

        return parameters;
    }

    private void setParametersToOperation(Operation operation, List<Parameter> parameters, OperationBuilderContext operationBuilderContext) {
        List<Parameter> filteredParameters = parameters.stream()
                .filter(parameter -> parameterPostFilters.stream().allMatch(filter -> filter.postAccept(parameter)))
                .collect(toList());
        ReferencedParametersConsumer referencedParametersConsumer = operationBuilderContext.getMapperContext().getReferenceConsumer(ReferencedParametersConsumer.class);
        setCollectionIfNotEmpty(filteredParameters, p -> referencedParametersConsumer.withOwner(operation).maybeAsReference(p, operation::setParameters));
    }

    private List<Parameter> getParametersFromHandlerMethod(OperationBuilderContext operationBuilderContext) {
        return operationBuilderContext.getOperationInfo().getHandlerMethod().getParameters().stream()
                .filter(methodParameter -> parameterPreFilters.stream().allMatch(filter -> filter.preAccept(methodParameter)))
                .flatMap(methodParameter -> {
                    OperationParameterCustomizerContext parameterCustomizerContext = operationParameterCustomizerContextFactory.create(operationBuilderContext, methodParameter);
                    // converters are ordered and the first one not returning null will be used
                    return firstNonNull(parameterMethodConverters, converter -> converter.convert(methodParameter))
                            // apply customizers after conversion
                            .map(parameter -> applyParameterCustomizers(parameter, parameterCustomizerContext))
                            .map(Stream::of).orElseGet(Stream::empty); // Optional.toStream()
                })
                .filter(parameter -> !parameter.isEmpty())
                .collect(toList());
    }

    private Parameter applyParameterCustomizers(Parameter parameter, OperationParameterCustomizerContext operationParameterCustomizerContext) {
        parameterCustomizers.forEach(customizer -> customizer.customize(parameter, operationParameterCustomizerContext));
        // return value for convenience in map
        return parameter;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
