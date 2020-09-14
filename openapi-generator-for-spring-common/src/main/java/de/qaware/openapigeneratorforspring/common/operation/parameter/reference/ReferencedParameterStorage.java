package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import io.swagger.v3.oas.models.parameters.Parameter;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ReferencedParameterStorage extends AbstractReferencedItemStorage<Parameter, ReferencedParameterStorage.Entry> {

    ReferencedParameterStorage(ReferenceDeciderForType<Parameter> referenceDecider, ReferenceNameFactoryForType<Parameter> referenceNameFactory, ReferenceNameConflictResolverForType<Parameter> referenceNameConflictResolver) {
        super(referenceDecider, referenceNameFactory, referenceNameConflictResolver, Entry::new);
    }

    void storeMaybeReference(Parameter parameter, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(parameter).addSetter(referenceNameConsumer);
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
