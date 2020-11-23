package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.operation.Callback;

@FunctionalInterface
public interface CallbackAnnotationMapper {
    Callback map(io.swagger.v3.oas.annotations.callbacks.Callback callbackAnnotation, MapperContext mapperContext);
}
