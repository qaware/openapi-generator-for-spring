package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceNameFactoryForType;
import io.swagger.v3.oas.models.responses.ApiResponse;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReferencedApiResponseStorage extends AbstractReferencedItemStorage<ApiResponse, ReferencedApiResponseStorage.Entry> {

    ReferencedApiResponseStorage(ReferenceDeciderForType<ApiResponse> referenceDecider, ReferenceNameFactoryForType<ApiResponse> referenceNameFactory, ReferenceNameConflictResolverForType<ApiResponse> referenceNameConflictResolver) {
        super(referenceDecider, referenceNameFactory, referenceNameConflictResolver, Entry::new);
    }

    void storeMaybeReference(String responseCode, ApiResponse apiResponse, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(apiResponse)
                .addResponseCode(responseCode)
                .addSetter(referenceNameConsumer);
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
