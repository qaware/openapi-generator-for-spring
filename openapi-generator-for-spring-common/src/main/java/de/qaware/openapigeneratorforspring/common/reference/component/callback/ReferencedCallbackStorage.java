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

package de.qaware.openapigeneratorforspring.common.reference.component.callback;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.operation.Callback;

import java.util.Arrays;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.API_RESPONSE;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.CALLBACK;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.PARAMETER;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.REQUEST_BODY;


public class ReferencedCallbackStorage extends AbstractReferencedItemStorage<Callback> {

    ReferencedCallbackStorage(ReferenceDeciderForType<Callback> referenceDecider, ReferenceIdentifierBuilderForType<Callback> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Callback> referenceIdentifierConflictResolver) {
        super(CALLBACK, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Callback::new,
                Arrays.asList(CALLBACK, API_RESPONSE, PARAMETER, REQUEST_BODY));
    }

    void storeMaybeReferenceCallbacks(Map<String, Callback> callbacks) {
        // no need for ownership as callbacks are referenced at only one place
        // this makes life easier than for example ApiResponses reference handling
        // here, we don't need to track the callback name together with the setter,
        // but simpler build a Set of callback names inside the entry
        callbacks.forEach((callbackName, callback) ->
                addEntry(callback, referenceCallback -> callbacks.put(callbackName, referenceCallback), AddEntryParameters.builder().suggestedIdentifier(callbackName).build())
        );
    }
}
