package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPreFilter;
import de.qaware.openapigeneratorforspring.common.mapper.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizerContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.component.parameter.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static java.util.stream.Collectors.toList;

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
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        // this customizer explicitly overrides already present parameters built from the operation annotation,
        // as the order of parameters is important and should not be determined
        // by the order of information to build those parameters!
        // TODO maybe consider making the above choice about parameter ordering better bean-customizable?
        List<Parameter> parameters = buildParameters(operationBuilderContext);
        setParametersToOperation(operation, parameters, operationBuilderContext);
    }

    private List<Parameter> buildParameters(OperationBuilderContext operationBuilderContext) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();
        Map<String, List<ParameterAnnotation>> parameterAnnotationsByName = Stream.concat(
                handlerMethod.findAnnotationsWithContext(io.swagger.v3.oas.annotations.Operation.class)
                        .map((annotation, context) -> Pair.of(annotation.parameters(), context))
                        .flatMap(pair -> Arrays.stream(pair.getLeft())
                                .map(annotation -> new ParameterAnnotation(annotation, pair.getRight()))
                        ),
                handlerMethod.findAnnotationsWithContext(io.swagger.v3.oas.annotations.Parameter.class)
                        .map(ParameterAnnotation::new)
        ).collect(OpenApiStreamUtils.groupingByPairKeyAndCollectingValuesToList());

        List<Parameter> parameters = new ArrayList<>();

        // first start with the parameters from the handler method, keep the order!
        for (Parameter parameterFromMethod : getParametersFromHandlerMethod(operationBuilderContext)) {
            List<ParameterAnnotation> parameterAnnotations = parameterAnnotationsByName.remove(parameterFromMethod.getName());
            if (parameterAnnotations != null) {
                parameterAnnotations.forEach(annotation -> parameterAnnotationMapper.applyFromAnnotation(
                        parameterFromMethod,
                        annotation.get(),
                        operationBuilderContext.getMapperContext(annotation.getContext())
                ));
            }
            parameters.add(parameterFromMethod);
        }

        // add leftover parameters from annotation only afterwards
        parameterAnnotationsByName.forEach((parameterName, parameterAnnotations) -> {
            // there must be at least one parameter annotation, as the map is a result of a groupingBy collector
            Iterator<ParameterAnnotation> iterator = parameterAnnotations.iterator();
            ParameterAnnotation firstAnnotation = iterator.next();
            Parameter parameter = parameterAnnotationMapper.buildFromAnnotation(firstAnnotation.get(),
                    operationBuilderContext.getMapperContext(firstAnnotation.getContext())
            );
            iterator.forEachRemaining(annotation -> parameterAnnotationMapper.applyFromAnnotation(parameter, annotation.get(),
                    operationBuilderContext.getMapperContext(annotation.getContext()))
            );
            // also customizers should be applied to parameters not coming from method parameters
            applyParameterCustomizers(parameter, OperationParameterCustomizerContextImpl.of(null, operationBuilderContext));
            setIfNotEmpty(parameter, parameters::add);
        });

        return preferRequiredParameters(parameters);
    }

    // TODO make this sorting customizable? See also other TODO here
    private static List<Parameter> preferRequiredParameters(List<Parameter> parameters) {
        Map<Boolean, List<Parameter>> partitionedParameters = parameters.stream()
                .collect(Collectors.partitioningBy(p -> Boolean.TRUE.equals(p.getRequired())));
        return Stream.concat(
                partitionedParameters.get(true).stream(),
                partitionedParameters.get(false).stream()
        ).collect(toList());
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    private static class ParameterAnnotation extends Pair<String, ParameterAnnotation> {
        private final transient io.swagger.v3.oas.annotations.Parameter annotation;
        @Getter
        private final transient HandlerMethod.Context context;

        public io.swagger.v3.oas.annotations.Parameter get() {
            return annotation;
        }

        @Override
        public String getLeft() {
            return annotation.name();
        }

        @Override
        public ParameterAnnotation getRight() {
            return this;
        }

        @Override
        public ParameterAnnotation setValue(ParameterAnnotation value) {
            return null;
        }
    }

    private void setParametersToOperation(Operation operation, List<Parameter> parameters, OperationBuilderContext operationBuilderContext) {
        List<Parameter> filteredParameters = parameters.stream()
                .filter(parameter -> parameterPostFilters.stream().allMatch(filter -> filter.postAccept(parameter)))
                .collect(toList());
        ReferencedParametersConsumer referencedParametersConsumer = operationBuilderContext.getReferencedItemConsumer(ReferencedParametersConsumer.class);
        setCollectionIfNotEmpty(filteredParameters, p -> referencedParametersConsumer.withOwner(operation).maybeAsReference(p, operation::setParameters));
    }

    private List<Parameter> getParametersFromHandlerMethod(OperationBuilderContext operationBuilderContext) {
        return operationBuilderContext.getOperationInfo().getHandlerMethod().getParameters().stream()
                .filter(methodParameter -> parameterPreFilters.stream().allMatch(filter -> filter.preAccept(methodParameter)))
                .flatMap(methodParameter -> {
                    OperationParameterCustomizerContext parameterCustomizerContext = OperationParameterCustomizerContextImpl.of(methodParameter, operationBuilderContext);
                    // converters are ordered and the first one not returning null will be used
                    return firstNonNull(parameterMethodConverters, converter -> converter.convert(methodParameter))
                            // apply customizers after conversion,
                            // first callback of specific parameter implementation,
                            .map(parameter -> applyCustomizeCallback(parameter, methodParameter))
                            // then parameter customizers applying to any implementation
                            .map(parameter -> applyParameterCustomizers(parameter, parameterCustomizerContext))
                            .map(Stream::of).orElseGet(Stream::empty); // Optional.toStream()
                })
                .filter(parameter -> !parameter.isEmpty())
                .collect(toList());
    }

    private Parameter applyCustomizeCallback(Parameter parameter, HandlerMethod.Parameter methodParameter) {
        methodParameter.customize(parameter);
        // return value for convenience in .map
        return parameter;
    }

    private Parameter applyParameterCustomizers(Parameter parameter, OperationParameterCustomizerContext operationParameterCustomizerContext) {
        parameterCustomizers.forEach(customizer -> customizer.customize(parameter, operationParameterCustomizerContext));
        // return value for convenience in .map
        return parameter;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
