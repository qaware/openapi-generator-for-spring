package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultOperationTagsCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        List<String> distinctTags = Stream.of(operationAnnotation.tags())
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (!distinctTags.isEmpty()) {
            operation.setTags(distinctTags);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
