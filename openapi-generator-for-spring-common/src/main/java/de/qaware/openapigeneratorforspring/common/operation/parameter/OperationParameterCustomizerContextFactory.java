package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizerContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;

@RequiredArgsConstructor
public class OperationParameterCustomizerContextFactory {
    private final List<HandlerMethod.MediaTypesParameterMapper> mediaTypesParameterMappers;

    public OperationParameterCustomizerContext create(OperationBuilderContext operationBuilderContext,
                                                      @Nullable HandlerMethod.Parameter handlerMethodParameter) {
        return new OperationParameterCustomizerContext() {
            @Override
            public OperationInfo getOperationInfo() {
                return operationBuilderContext.getOperationInfo();
            }

            @Override
            public MapperContext getMapperContext() {
                return operationBuilderContext.getMapperContext()
                        .withSuggestedMediaTypesSupplierFor(Parameter.class, () -> firstNonNull(mediaTypesParameterMappers, mapper -> mapper.map(handlerMethodParameter))
                                .orElseThrow(() -> new IllegalStateException("Cannot find media types for " + handlerMethodParameter))
                        );
            }

            @Override
            public Optional<HandlerMethod.Parameter> getHandlerMethodParameter() {
                return Optional.ofNullable(handlerMethodParameter);
            }
        };
    }


}
