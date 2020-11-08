package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.filter.operation.OperationPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationPreFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.operation.OperationWithInfo;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import de.qaware.openapigeneratorforspring.model.path.RequestMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PathItemBuilderFactory {

    private final OperationBuilder operationBuilder;
    private final List<OperationPreFilter> operationPreFilters;
    private final List<OperationPostFilter> operationPostFilters;
    private final List<PathItemCustomizer> pathItemCustomizers;

    public PathItemBuilder create(ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return new PathItemBuilder(referencedItemConsumerSupplier);
    }

    @RequiredArgsConstructor
    public class PathItemBuilder {
        private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;

        @Getter
        private final MultiValueMap<String, OperationWithInfo> operationsById = new LinkedMultiValueMap<>();

        public PathItem build(String pathPattern, Map<RequestMethod, HandlerMethod> handlerMethods) {
            PathItem pathItem = new PathItem();
            handlerMethods.forEach((requestMethod, handlerMethod) -> {
                OperationInfo operationInfo = OperationInfo.of(handlerMethod, requestMethod, pathPattern);
                if (isNotAcceptedByAllOperationPreFilters(operationInfo)) {
                    return;
                }
                Operation operation = operationBuilder.buildOperation(operationInfo, referencedItemConsumerSupplier);
                if (isAcceptedByAllOperationPostFilters(operation, handlerMethod)) {
                    String operationId = operation.getOperationId();
                    if (operationId != null) {
                        operationsById.add(operationId, OperationWithInfo.of(operation, operationInfo));
                    }
                    pathItem.setOperation(requestMethod, operation);
                }
            });
            pathItemCustomizers.forEach(pathItemCustomizer -> pathItemCustomizer.customize(pathItem, pathPattern, referencedItemConsumerSupplier));
            return pathItem;
        }

        private boolean isNotAcceptedByAllOperationPreFilters(OperationInfo operationInfo) {
            return !operationPreFilters.stream().allMatch(operationPostFilter -> operationPostFilter.preAccept(operationInfo));
        }

        private boolean isAcceptedByAllOperationPostFilters(Operation operation, HandlerMethod handlerMethod) {
            return operationPostFilters.stream().allMatch(operationPostFilter -> operationPostFilter.postAccept(operation, handlerMethod));
        }
    }
}
