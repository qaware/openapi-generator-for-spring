package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ReferencedParameterStorage extends AbstractReferencedItemStorage<Parameter, ReferencedParameterStorage.Entry> {

    ReferencedParameterStorage(ReferenceDeciderForType<Parameter> referenceDecider, ReferenceIdentifierFactoryForType<Parameter> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Parameter> referenceIdentifierConflictResolver) {
        super(ReferenceType.PARAMETER, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Parameter::new, Entry::new);
    }

    void storeMaybeReference(Parameter parameter, Consumer<Parameter> setter) {
        getEntryOrAddNew(parameter).addSetter(setter);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<Parameter> {
        protected Entry(Parameter item) {
            super(item);
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return getItem().getName();
        }
    }
}
