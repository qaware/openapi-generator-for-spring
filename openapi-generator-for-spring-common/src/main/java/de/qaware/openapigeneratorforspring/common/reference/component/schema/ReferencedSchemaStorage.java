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

package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Collections;
import java.util.function.Consumer;

public class ReferencedSchemaStorage extends AbstractReferencedItemStorage<Schema> {

    ReferencedSchemaStorage(ReferenceDeciderForType<Schema> referenceDecider,
                            ReferenceIdentifierBuilderForType<Schema> referenceIdentifierFactory,
                            ReferenceIdentifierConflictResolverForType<Schema> referenceIdentifierConflictResolver) {
        super(ReferenceType.SCHEMA, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Schema::new,
                Collections.emptyList());
    }

    void storeAlwaysReference(Schema schema, Consumer<Schema> setter) {
        addEntry(schema, setter, AddEntryParameters.builder()
                .referenceRequired(true)
                .suggestedIdentifier(schema.getName())
                .build()
        );
    }

    void storeMaybeReference(Schema schema, Consumer<Schema> setter) {
        addEntry(schema, setter, AddEntryParameters.builder()
                .suggestedIdentifier(schema.getName())
                .build()
        );
    }
}
