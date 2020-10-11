package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

public class DefaultPathItemParametersCustomizer implements PathItemCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public void customize(PathItem pathItem, String pathPattern,
                          ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        List<Parameter> sharedParameters = new ArrayList<>();
        ReferencedParametersConsumer referencedParametersConsumer = referencedItemConsumerSupplier.get(ReferencedParametersConsumer.class);
        Optional.ofNullable(pathItem.getOperations())
                .map(Map::values)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .flatMap(operation ->
                        Optional.ofNullable(operation.getParameters())
                                .map(Collection::stream).orElseGet(Stream::empty)
                                .map(parameter -> Pair.of(parameter, operation))
                )
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())))
                .forEach((parameter, operations) -> {
                    if (operations.size() > 1) {
                        operations.forEach(operation -> {
                            if (!operation.getParameters().removeIf(parameter::equals)) {
                                // if this happens, the EqualsHashCode of Parameter is most likely broken
                                throw new IllegalStateException("Did not find parameter " + parameter + " in " + operation.getParameters());
                            }
                            // need to call reference consumer even if list is empty
                            // to drop possibly existing reference setters
                            referencedParametersConsumer.withOwner(operation).maybeAsReference(operation.getParameters(), operation::setParameters);
                            // remove parameters property from model entirely if it has become empty
                            if (operation.getParameters().isEmpty()) {
                                operation.setParameters(null);
                            }
                        });
                        sharedParameters.add(parameter);
                    }
                });
        setCollectionIfNotEmpty(sharedParameters,
                parameters -> referencedParametersConsumer.withOwner(pathItem).maybeAsReference(parameters, pathItem::setParameters)
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
