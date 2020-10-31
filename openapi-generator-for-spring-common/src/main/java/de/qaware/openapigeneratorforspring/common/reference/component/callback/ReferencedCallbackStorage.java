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
                addEntry(callback, referenceCallback -> callbacks.put(callbackName, referenceCallback), callbackName)
        );
    }
}
