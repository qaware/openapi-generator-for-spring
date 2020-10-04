package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.filter.operation.OperationFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.operation.OperationWithInfo;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class PathItemBuilderFactory {

    private final OperationBuilder operationBuilder;
    private final List<OperationFilter> operationFilters;

    public PathItemBuilder create(ReferencedItemConsumerSupplier referencedItemConsumerSupplier, Consumer<List<Tag>> tagsConsumer) {
        return new PathItemBuilder(referencedItemConsumerSupplier, tagsConsumer);
    }

    @RequiredArgsConstructor
    public class PathItemBuilder {
        private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;
        private final Consumer<List<Tag>> tagsConsumer;

        @Getter
        private final Map<RequestMethod, Operation> operationPerMethod = new EnumMap<>(RequestMethod.class);
        @Getter
        private final MultiValueMap<String, OperationWithInfo> operationsByIdPerPathItem = new LinkedMultiValueMap<>();

        public PathItem build(String pathPattern, HandlerMethodWithInfo handlerMethodWithInfo) {
            PathItem pathItem = new PathItem();
            handlerMethodWithInfo.getRequestMethods().forEach(requestMethod -> {
                HandlerMethod handlerMethod = handlerMethodWithInfo.getHandlerMethod();
                OperationInfo operationInfo = OperationInfo.of(handlerMethod, requestMethod, pathPattern);
                OperationBuilderContext operationBuilderContext = new OperationBuilderContext(operationInfo, referencedItemConsumerSupplier, tagsConsumer);
                Operation operation = operationBuilder.buildOperation(operationBuilderContext);
                if (isAcceptedByAllOperationFilters(operation, handlerMethod)) {
                    String operationId = operation.getOperationId();
                    if (operationId != null) {
                        operationsByIdPerPathItem.add(operationId, OperationWithInfo.of(operation, operationInfo));
                    }
                    operationPerMethod.put(requestMethod, operation);
                    pathItem.addOperation(requestMethod.name(), operation);
                }
            });
            return pathItem;
        }

        private boolean isAcceptedByAllOperationFilters(Operation operation, HandlerMethod handlerMethod) {
            return operationFilters.stream().allMatch(operationFilter -> operationFilter.accept(operation, handlerMethod));
        }
    }
}
