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

package de.qaware.openapigeneratorforspring.common.reference.component.header;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.header.Header;

import java.util.Arrays;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.EXAMPLE;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.HEADER;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.SCHEMA;


public class ReferencedHeaderStorage extends AbstractReferencedItemStorage<Header> {

    ReferencedHeaderStorage(ReferenceDeciderForType<Header> referenceDecider, ReferenceIdentifierBuilderForType<Header> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Header> referenceIdentifierConflictResolver) {
        super(HEADER, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Header::new,
                Arrays.asList(HEADER, SCHEMA, EXAMPLE));
    }

    public void maybeReferenceHeaders(Map<String, Header> headers) {
        addEntriesFromMap(headers);
    }
}
