/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.parameter.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import lombok.val;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Shared items are {@link PathItem#getParameters} and {@link PathItem#getServers}.
 * They are collected from operations as part of the given path item.
 */
public class DefaultPathItemSharedItemsCustomizer implements PathItemCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public void customize(PathItem pathItem, String pathPattern,
                          ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        if (pathItem.getOperations().size() < 2) {
            return;
        }
        customizeSharedServers(pathItem);
        customizeSharedParameters(pathItem, referencedItemConsumerSupplier);
    }

    private static void customizeSharedServers(PathItem pathItem) {
        // we only share servers among operations if the given sharedServers is in all operations
        // the spec says that descendant server object overrides any parent server objects
        val operations = pathItem.getOperations().values();
        val sharedServersInAllOperations = operations.stream()
                .map(Operation::getServers)
                .filter(Objects::nonNull)
                .reduce((a, b) -> a.equals(b) ? a : Collections.emptyList())
                .orElseGet(Collections::emptyList);
        if (!sharedServersInAllOperations.isEmpty()) {
            operations.forEach(operation -> operation.setServers(null));
            pathItem.setServers(sharedServersInAllOperations);
        }
    }

    private static void customizeSharedParameters(PathItem pathItem, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        val operations = pathItem.getOperations().values();
        val sharedParameters = findSharedParametersAmongOperations(operations);
        if (!sharedParameters.isEmpty()) {
            val referencedParametersConsumer = referencedItemConsumerSupplier.get(ReferencedParametersConsumer.class);
            operations.forEach(operation -> {
                operation.getParameters().removeAll(sharedParameters);
                // owner update must be called even if operation parameters are empty
                // to move ownership properly from operation to path item
                referencedParametersConsumer.withOwner(operation).maybeAsReference(operation.getParameters(), operation::setParameters);
                if (operation.getParameters().isEmpty()) {
                    // make the parameters of single operation disappear completely if empty
                    operation.setParameters(null);
                }
            });
            // eventually move ownership from operation to path item
            referencedParametersConsumer.withOwner(pathItem).maybeAsReference(sharedParameters, pathItem::setParameters);
        }
    }

    private static List<Parameter> findSharedParametersAmongOperations(Collection<Operation> operations) {
        return operations.stream()
                .map(operation -> Optional.ofNullable(operation.getParameters())
                        .<List<Parameter>>map(ArrayList::new) // make a modifiable copy for retainAll
                        .orElseGet(Collections::emptyList)
                )
                .reduce((a, b) -> {
                    a.retainAll(b);
                    return a;
                })
                .orElseGet(Collections::emptyList);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
