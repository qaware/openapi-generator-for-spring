package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.filter.operation.OperationFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.operation.OperationWithInfo;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PathItemBuilderFactory {

    private final OperationBuilder operationBuilder;
    private final List<OperationFilter> operationFilters;
    private final List<PathItemCustomizer> pathItemCustomizers;

    public PathItemBuilder create(ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return new PathItemBuilder(referencedItemConsumerSupplier);
    }

    @RequiredArgsConstructor
    public class PathItemBuilder {
        private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;

        @Getter
        private final Map<RequestMethod, Operation> operationPerMethod = new EnumMap<>(RequestMethod.class);
        @Getter
        private final MultiValueMap<String, OperationWithInfo> operationsById = new LinkedMultiValueMap<>();

        public PathItem build(String pathPattern, Map<RequestMethod, HandlerMethod> handlerMethods) {
            PathItem pathItem = new PathItem();
            handlerMethods.forEach((requestMethod, handlerMethod) -> {
                OperationInfo operationInfo = OperationInfo.of(handlerMethod, requestMethod, pathPattern);
                OperationBuilderContext operationBuilderContext = new OperationBuilderContext(operationInfo, referencedItemConsumerSupplier);
                Operation operation = operationBuilder.buildOperation(operationBuilderContext);
                if (isAcceptedByAllOperationFilters(operation, handlerMethod)) {
                    String operationId = operation.getOperationId();
                    if (operationId != null) {
                        operationsById.add(operationId, OperationWithInfo.of(operation, operationInfo));
                    }
                    operationPerMethod.put(requestMethod, operation);
                    pathItem.addOperation(requestMethod.name(), operation);
                }
            });
            pathItemCustomizers.forEach(pathItemCustomizer -> pathItemCustomizer.customize(pathItem, pathPattern, referencedItemConsumerSupplier));
            return pathItem;
        }

        private boolean isAcceptedByAllOperationFilters(Operation operation, HandlerMethod handlerMethod) {
            return operationFilters.stream().allMatch(operationFilter -> operationFilter.accept(operation, handlerMethod));
        }
    }
}
