/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
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
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.path.PathItem;

/**
 * Customizer for {@link PathItem path item} of the
 * OpenApi model. Is run AFTER the path item is built.
 */
@FunctionalInterface
public interface PathItemCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize the given path item by reference.
     *
     * @param pathItem                       path item
     * @param pathPattern                    path pattern for this path item
     * @param referencedItemConsumerSupplier for referencing additional items or adapt the referencing
     */
    void customize(PathItem pathItem, String pathPattern, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}
