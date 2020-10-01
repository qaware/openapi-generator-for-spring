package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReferencedApiResponseStorage extends AbstractReferencedItemStorage<ApiResponse, ReferencedApiResponseStorage.Entry> {

    ReferencedApiResponseStorage(ReferenceDeciderForType<ApiResponse> referenceDecider, ReferenceIdentifierFactoryForType<ApiResponse> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<ApiResponse> referenceIdentifierConflictResolver) {
        super(ReferenceType.API_RESPONSE, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Entry::new);
    }

    void storeMaybeReference(String responseCode, ApiResponse apiResponse, Consumer<String> setter) {
        getEntryOrAddNew(apiResponse)
                .addResponseCode(responseCode)
                .addSetter(setter);
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntry<ApiResponse> {

        private final Set<String> responseCodes = new HashSet<>();

        protected Entry(ApiResponse item) {
            super(item);
        }

        public Entry addResponseCode(String responseCode) {
            responseCodes.add(responseCode);
            return this; // fluent API
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return responseCodes.stream().sorted().collect(Collectors.joining("_"));
        }
    }
}
