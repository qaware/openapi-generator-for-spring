package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.operation.Callback;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringRequireNonBlank;

@RequiredArgsConstructor
public class DefaultCallbackAnnotationMapper implements CallbackAnnotationMapper {
    private final OperationAnnotationMapper operationAnnotationMapper;

    @Override
    public Callback map(io.swagger.v3.oas.annotations.callbacks.Callback callbackAnnotation, MapperContext mapperContext) {
        Callback callback = new Callback();
        setStringRequireNonBlank(callbackAnnotation.callbackUrlExpression(), callback::setCallbackUrlExpression);
        Map<String, Operation> operations = buildStringMapFromStream(
                Arrays.stream(callbackAnnotation.operation()),
                io.swagger.v3.oas.annotations.Operation::method,
                operationAnnotation -> operationAnnotationMapper.buildFromAnnotation(operationAnnotation, mapperContext)
        );
        if (!operations.isEmpty()) {
            PathItem pathItem = new PathItem();
            pathItem.setOperations(operations);
            callback.setPathItem(pathItem);
        }
        setStringIfNotBlank(callbackAnnotation.ref(), callback::setRef);
        return callback;
    }
}
