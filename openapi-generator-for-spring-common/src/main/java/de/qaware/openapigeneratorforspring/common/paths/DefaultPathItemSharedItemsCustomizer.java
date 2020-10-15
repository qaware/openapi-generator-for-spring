package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.parameter.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotNull;

/**
 * Shared items are {@link PathItem#parameters} and {@link PathItem#servers}.
 * They are collected from operations as part of the given path item.
 */
public class DefaultPathItemSharedItemsCustomizer implements PathItemCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public void customize(PathItem pathItem, String pathPattern,
                          ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        // servers
        setCollectionIfNotEmpty(findSharedItemsAmongOperations(pathItem, Operation::getServers,
                (sharedServer, operations) -> {
                    // we only share a server among operations if the given sharedServer in all operations
                    // the spec says that descendant server object overrides any parent server objects
                    boolean sharedServerIsTheOnlyEntry = operations.stream().map(Operation::getServers)
                            .allMatch(servers -> servers.size() == 1 && servers.contains(sharedServer));
                    if (sharedServerIsTheOnlyEntry) {
                        operations.forEach(operation -> operation.setServers(null));
                        return sharedServer;
                    }
                    return null; // indicate we can't share this server
                }
                ),
                pathItem::setServers
        );

        // parameters
        ReferencedParametersConsumer referencedParametersConsumer = referencedItemConsumerSupplier.get(ReferencedParametersConsumer.class);
        setCollectionIfNotEmpty(findSharedItemsAmongOperations(pathItem, Operation::getParameters,
                (sharedParameter, operations) -> {
                    operations.forEach(operation -> {
                        operation.getParameters().removeIf(sharedParameter::equals);
                        // must be called even if parameters are empty to move ownership properly from operation to path item
                        referencedParametersConsumer.withOwner(operation).maybeAsReference(operation.getParameters(), operation::setParameters);
                        if (operation.getParameters().isEmpty()) {
                            operation.setParameters(null);
                        }
                    });
                    // parameters can always be shared as they add up to descendants
                    return sharedParameter;
                }),
                // eventually move ownership from operation to path item
                sharedParameters -> referencedParametersConsumer.withOwner(pathItem).maybeAsReference(sharedParameters, pathItem::setParameters)
        );
    }

    protected static <T> List<T> findSharedItemsAmongOperations(PathItem pathItem,
                                                                Function<Operation, List<T>> itemsGetter,
                                                                BiFunction<T, List<Operation>, T> sharedItemConsumer
    ) {
        List<T> sharedItems = new ArrayList<>();
        Optional.ofNullable(pathItem.getOperations())
                .map(Map::values)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .flatMap(operation ->
                        Optional.ofNullable(itemsGetter.apply(operation))
                                .map(Collection::stream).orElseGet(Stream::empty)
                                .map(item -> Pair.of(item, operation))
                )
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())))
                .forEach((item, operations) -> {
                    if (operations.size() > 1) {
                        // sharedItemConsumer may decide if it becomes part of the shared items
                        // by returning non-null item
                        setIfNotNull(sharedItemConsumer.apply(item, operations), sharedItems::add);
                    }
                });
        return sharedItems;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
