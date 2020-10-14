package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ReferencedParameterStorage extends AbstractReferencedItemStorage<Parameter, ReferencedParameterStorage.Entry> {

    ReferencedParameterStorage(ReferenceDeciderForType<Parameter> referenceDecider, ReferenceIdentifierFactoryForType<Parameter> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Parameter> referenceIdentifierConflictResolver) {
        super(ReferenceType.PARAMETER, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Parameter::new, Entry::new);
    }

    void maybeReferenceParameters(List<Parameter> parameters, Object owner) {
        // we use the fact that we can modify each parameter via index if it's supposed to be changed to a reference
        // we still need to remove previously set parameters to the same owner then
        checkOwner(owner);
        removeReferenceSettersOwnedBy(owner);
        // using an IntStream here makes i effectively final and thus can be captured in nested lambda
        IntStream.range(0, parameters.size()).forEach(i ->
                getEntryOrAddNew(parameters.get(i)).addSetter(ParameterReferenceSetter.of(
                        parameter -> parameters.set(i, parameter),
                        owner
                ))
        );
    }

    private void checkOwner(Object owner) {
        if (owner instanceof Operation) {
            return;
        }
        if (owner instanceof PathItem) {
            return;
        }
        throw new IllegalStateException("Owner of parameter list is invalid: " + owner);
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class ParameterReferenceSetter implements AbstractReferencedItemStorage.ReferenceSetter<Parameter> {
        private final Consumer<Parameter> setter;
        @Getter
        private final Object owner;

        @Override
        public void consumeReference(Parameter referenceItem) {
            setter.accept(referenceItem);
        }
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
