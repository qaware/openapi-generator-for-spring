package de.qaware.openapigeneratorforspring.common.reference.component.callback;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.operation.Callback;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


public class ReferencedCallbackStorage extends AbstractReferencedItemStorage<Callback, ReferencedCallbackStorage.Entry> {

    ReferencedCallbackStorage(ReferenceDeciderForType<Callback> referenceDecider, ReferenceIdentifierFactoryForType<Callback> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Callback> referenceIdentifierConflictResolver) {
        super(ReferenceType.CALLBACK, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Callback::new, Entry::new);
    }

    void storeMaybeReferenceCallbacks(Map<String, Callback> callbacks) {
        // no need for ownership as callbacks are referenced at only one place
        // this makes life easier than for example ApiResponses reference handling
        // here, we don't need to track the callback name together with the setter,
        // but simpler build a Set of callback names inside the entry
        callbacks.forEach((callbackName, callback) ->
                getEntryOrAddNew(callback)
                        .addCallbackName(callbackName)
                        .addSetter(referenceCallback -> callbacks.put(callbackName, referenceCallback))
        );
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Callback> {
        protected Entry(Callback item) {
            super(item);
        }

        private final Set<String> callbackNames = new LinkedHashSet<>();

        @Override
        public String getSuggestedIdentifier() {
            return String.join("_", callbackNames);
        }

        public AbstractReferencableEntry<Callback> addCallbackName(String callbackName) {
            callbackNames.add(callbackName);
            return this; // fluent API
        }
    }
}
