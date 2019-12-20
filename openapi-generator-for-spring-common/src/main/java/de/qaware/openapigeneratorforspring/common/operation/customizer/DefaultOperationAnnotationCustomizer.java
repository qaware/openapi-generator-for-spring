package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultOperationAnnotationCustomizer implements OperationCustomizer, Ordered {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {

        Method method = operationBuilderContext.getHandlerMethod().getMethod();

        io.swagger.v3.oas.annotations.Operation operationAnnotation
                = AnnotationUtils.findAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);

        if (operationAnnotation != null) {
            setIfNotBlank(operationAnnotation.summary(), operation::setSummary);
            setIfNotBlank(operationAnnotation.description(), operation::setDescription);
            setIfNotBlank(operationAnnotation.operationId(), operation::setOperationId);
            if (operationAnnotation.deprecated()) {
                operation.setDeprecated(true);
            }
            List<String> distinctTags = Stream.of(operationAnnotation.tags())
                    .distinct()
                    .collect(Collectors.toList());
            operation.setTags(distinctTags);
        }
    }

    private static void setIfNotBlank(@Nullable String value, Consumer<String> setter) {
        if (StringUtils.isNotBlank(value)) {
            setter.accept(value);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
