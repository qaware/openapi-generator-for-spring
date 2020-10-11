package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter;
import de.qaware.openapigeneratorforspring.common.filter.pathitem.PathItemFilter;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PathsBuilder {

    private final HandlerMethodsProvider handlerMethodsProvider;
    private final PathItemBuilderFactory pathItemBuilderFactory;
    private final List<PathItemFilter> pathItemFilters;
    private final List<HandlerMethodFilter> handlerMethodFilters;
    private final OperationIdConflictResolver operationIdConflictResolver;


    public Paths buildPaths(ReferencedItemConsumerSupplier referencedItemConsumerSupplier, Consumer<List<Tag>> tagsConsumer) {
        MultiValueMap<String, OperationWithInfo> operationsById = new LinkedMultiValueMap<>();
        Paths paths = new Paths();
        handlerMethodsProvider.getHandlerMethods().stream()
                .flatMap(handlerMethodWithInfo ->
                        handlerMethodWithInfo.getPathPatterns().stream()
                                .map(pathPattern -> Pair.of(pathPattern, handlerMethodWithInfo))
                )
                .filter(item -> isAcceptedByAllHandlerMethodFilters(item.getRight()))
                .sorted(Comparator.comparing(Pair::getLeft))
                // there might be more than one handlerWithInfo with the same pathPattern,
                // so we need to re-group this here
                .collect(Collectors.groupingBy(Pair::getLeft, LinkedHashMap::new, Collectors.mapping(Pair::getRight, Collectors.toList())))
                .forEach((pathPattern, handlerMethodWithInfos) -> {

                    Map<RequestMethod, HandlerMethod> handlerMethods = handlerMethodWithInfos.stream()
                            .flatMap(handlerMethodWithInfo -> handlerMethodWithInfo.getRequestMethods().stream()
                                    .map(requestMethod -> Pair.of(requestMethod, handlerMethodWithInfo.getHandlerMethod())))
                            .sorted(Map.Entry.comparingByKey()) // natural order of the enum RequestMethod
                            .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> {
                                throw new IllegalStateException("Found more than one request handler method for path " + pathPattern + ": " + a + " vs. " + b);
                            }, LinkedHashMap::new));

                    PathItemBuilderFactory.PathItemBuilder pathItemBuilder = pathItemBuilderFactory.create(referencedItemConsumerSupplier, tagsConsumer);
                    PathItem pathItem = pathItemBuilder.build(pathPattern, handlerMethods);
                    if (isAcceptedByAllPathItemFilters(pathItem, pathPattern, pathItemBuilder.getOperationPerMethod())) {
                        operationsById.addAll(pathItemBuilder.getOperationsById());
                        paths.put(pathPattern, pathItem);
                    }
                });
        resolveOperationIdConflicts(operationsById);
        return paths;
    }

    private void resolveOperationIdConflicts(MultiValueMap<String, OperationWithInfo> operationsById) {
        operationsById.forEach((operationId, operationsWithInfo) -> {
            // more than one operation with same id is a conflict
            if (operationsWithInfo.size() > 1) {
                // more than one operation per ID is a conflict which should be resolved!
                operationIdConflictResolver.resolveConflict(operationId, operationsWithInfo);
                checkIfOperationIdConflictIsResolved(operationsById, operationId, operationsWithInfo);
            }
        });
    }

    private void checkIfOperationIdConflictIsResolved(MultiValueMap<String, OperationWithInfo> operationsById, String operationId, List<OperationWithInfo> operationsWithInfo) {
        Set<String> uniqueOperationIds = operationsWithInfo.stream()
                .map(OperationWithInfo::getOperation)
                .map(Operation::getOperationId)
                .collect(Collectors.toSet());
        if (uniqueOperationIds.size() != operationsWithInfo.size()) {
            throw new IllegalStateException("The operation id conflict resolution for " + operationId + " did not generate unique IDs but " + uniqueOperationIds);
        }
        uniqueOperationIds.stream()
                .filter(uniqueOperationId -> !uniqueOperationId.equals(operationId))
                .forEach(uniqueOperationId -> {
                    if (operationsById.containsKey(uniqueOperationId)) {
                        throw new IllegalStateException("The operation id conflict resolution for " + operationId + " generated an already used unique id: " + uniqueOperationId);
                    }
                });
    }

    private boolean isAcceptedByAllHandlerMethodFilters(HandlerMethodWithInfo handlerMethod) {
        return handlerMethodFilters.stream().allMatch(handlerMethodFilter -> handlerMethodFilter.accept(handlerMethod));
    }

    private boolean isAcceptedByAllPathItemFilters(PathItem pathItem, String pathPattern, Map<RequestMethod, Operation> operationPerMethod) {
        return pathItemFilters.stream().allMatch(pathItemFilter -> pathItemFilter.accept(pathItem, pathPattern, operationPerMethod));
    }
}
