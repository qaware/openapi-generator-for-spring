package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ReferencedParameterStorage extends AbstractReferencedItemStorage<Parameter, ReferencedParameterStorage.ReferencableParameterEntry> {

    ReferencedParameterStorage(ReferenceDeciderForType<Parameter> referenceDecider, ReferenceNameFactoryForType<Parameter> referenceNameFactory, ReferenceNameConflictResolverForType<Parameter> referenceNameConflictResolver) {
        super(referenceDecider, referenceNameFactory, referenceNameConflictResolver, ReferencableParameterEntry::new);
    }

    void storeParameterMaybeReference(Parameter parameter, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(parameter).referenceNameSetters.add(referenceNameConsumer);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class ReferencableParameterEntry implements AbstractReferencedItemStorage.ReferencableEntry<Parameter> {

        private final Parameter parameter;
        private final List<Consumer<ReferenceName>> referenceNameSetters = new ArrayList<>();

        @Override
        public Parameter getItem() {
            return parameter;
        }

        @Override
        public boolean matches(Parameter item) {
            return parameter.equals(item);
        }

        @Override
        public int getNumberOfUsages() {
            return referenceNameSetters.size();
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return parameter.getName();
        }

        @Override
        public Stream<Consumer<ReferenceName>> getReferenceNameSetters() {
            return referenceNameSetters.stream();
        }
    }
}
