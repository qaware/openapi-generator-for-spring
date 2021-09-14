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
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import lombok.val;

import java.util.Collections;
import java.util.Objects;

/**
 * Shared servers are collected from operations as part of the given path item.
 */
public class DefaultPathItemSharedServersCustomizer implements PathItemCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public void customize(PathItem pathItem, String pathPattern,
                          ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        if (pathItem.getOperations().size() < 2) {
            return;
        }
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

    @Override
    public int getOrder() {
        return ORDER;
    }
}
