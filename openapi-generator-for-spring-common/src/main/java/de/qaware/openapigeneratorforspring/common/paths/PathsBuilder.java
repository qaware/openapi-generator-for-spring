package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.PathItemFilter;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilder;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.operation.OperationWithInfo;
import de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import de.qaware.openapigeneratorforspring.model.path.Paths;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class PathsBuilder {

    private final HandlerMethodsProvider handlerMethodsProvider;
    private final OperationBuilder operationBuilder;
    private final List<PathItemFilter> pathItemFilters;
    private final List<HandlerMethodFilter> handlerMethodFilters;
    private final List<OperationFilter> operationFilters;
    private final OperationIdConflictResolver operationIdConflictResolver;

    public Paths buildPaths(ReferencedItemConsumerSupplier referencedItemConsumerSupplier, Consumer<List<Tag>> tagsConsumer) {

        List<HandlerMethodWithInfo> handlerMethods = handlerMethodsProvider.getHandlerMethods();

        MultiValueMap<String, OperationWithInfo> operationsById = new LinkedMultiValueMap<>();

        Paths paths = new Paths();

        handlerMethods.stream()
                .flatMap(handlerMethodWithInfo ->
                        handlerMethodWithInfo.getPathPatterns().stream()
                                .map(pathPattern -> Pair.of(pathPattern, handlerMethodWithInfo))
                )
                .filter(item -> isAcceptedByAllHandlerMethodFilters(item.getRight()))
                .sorted(Comparator.comparing(Pair::getLeft))
                .forEachOrdered(item -> {
                    String pathPattern = item.getLeft();
                    HandlerMethodWithInfo handlerMethodWithInfo = item.getRight();

                    PathItem pathItem = new PathItem();
                    Set<RequestMethod> requestMethods = handlerMethodWithInfo.getRequestMethods();
                    Map<RequestMethod, Operation> operationPerMethod = new EnumMap<>(RequestMethod.class);
                    MultiValueMap<String, OperationWithInfo> operationsByIdPerPathItem = new LinkedMultiValueMap<>();

                    requestMethods.forEach(requestMethod -> {
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
                            setOperationOnPathItem(requestMethod, pathItem, operation);
                        }
                    });

                    if (isAcceptedByAllPathFilters(pathItem, pathPattern, operationPerMethod)) {
                        operationsById.addAll(operationsByIdPerPathItem);
                        paths.put(pathPattern, pathItem);
                    }
                });

        operationsById.forEach((operationId, operationsWithInfo) -> {
            if (operationsWithInfo.size() > 1) {
                // more than one operation per ID is a conflict which should be resolved!
                operationIdConflictResolver.resolveConflict(operationId, operationsWithInfo);
                // TODO verify that the conflict has been resolved?
            }
        });


        return paths;
    }

    private boolean isAcceptedByAllHandlerMethodFilters(HandlerMethodWithInfo handlerMethod) {
        return handlerMethodFilters.stream().allMatch(handlerMethodFilter -> handlerMethodFilter.accept(handlerMethod));
    }

    private boolean isAcceptedByAllOperationFilters(Operation operation, HandlerMethod handlerMethod) {
        return operationFilters.stream().allMatch(operationFilter -> operationFilter.accept(operation, handlerMethod));
    }

    private boolean isAcceptedByAllPathFilters(PathItem pathItem, String pathPattern, Map<RequestMethod, Operation> operationPerMethod) {
        return pathItemFilters.stream().allMatch(pathItemFilter -> pathItemFilter.accept(pathItem, pathPattern, operationPerMethod));
    }

    private static void setOperationOnPathItem(RequestMethod requestMethod, PathItem pathItem, Operation operation) {
        switch (requestMethod) {
            case GET:
                pathItem.setGet(operation);
                break;
            case HEAD:
                pathItem.setHead(operation);
                break;
            case POST:
                pathItem.setPost(operation);
                break;
            case PUT:
                pathItem.setPut(operation);
                break;
            case PATCH:
                pathItem.setPatch(operation);
                break;
            case DELETE:
                pathItem.setDelete(operation);
                break;
            case OPTIONS:
                pathItem.setOptions(operation);
                break;
            case TRACE:
                pathItem.setTrace(operation);
                break;
        }
    }
}
