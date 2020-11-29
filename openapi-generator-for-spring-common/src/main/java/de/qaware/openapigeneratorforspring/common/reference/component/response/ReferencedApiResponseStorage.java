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

package de.qaware.openapigeneratorforspring.common.reference.component.response;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;

import java.util.Arrays;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.EXAMPLE;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.HEADER;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.LINK;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.SCHEMA;

public class ReferencedApiResponseStorage extends AbstractReferencedItemStorage<ApiResponse> {
    ReferencedApiResponseStorage(ReferenceDeciderForType<ApiResponse> referenceDecider, ReferenceIdentifierBuilderForType<ApiResponse> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<ApiResponse> referenceIdentifierConflictResolver) {
        super(ReferenceType.API_RESPONSE, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, ApiResponse::new,
                Arrays.asList(SCHEMA, EXAMPLE, HEADER, LINK));
    }

    void maybeReferenceApiResponses(ApiResponses apiResponses) {
        // exploit the fact that we've access to the full map of apiResponses
        // that means we can modify the map as a reference
        // if this method is called again for the same owner instance of apiResponses,
        // any previously defined reference consumers need to be removed first
        removeEntriesOwnedBy(apiResponses);
        addEntriesFromMap(apiResponses);
    }
}
