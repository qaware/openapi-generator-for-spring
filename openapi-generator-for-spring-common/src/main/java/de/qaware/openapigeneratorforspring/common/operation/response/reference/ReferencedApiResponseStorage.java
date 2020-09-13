package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactoryForType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReferencedApiResponseStorage extends AbstractReferencedItemStorage<ApiResponse, ReferencedApiResponseStorage.ReferencableApiResponseEntry> {

    ReferencedApiResponseStorage(ReferenceDeciderForType<ApiResponse> referenceDecider, ReferenceNameFactoryForType<ApiResponse> referenceNameFactory, ReferenceNameConflictResolverForType<ApiResponse> referenceNameConflictResolver) {
        super(referenceDecider, referenceNameFactory, referenceNameConflictResolver, ReferencableApiResponseEntry::new);
    }

    void storeApiResponseMaybeReference(String responseCode, ApiResponse apiResponse, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(apiResponse)
                .withResponseCode(responseCode)
                .referenceNameSetters.add(referenceNameConsumer);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class ReferencableApiResponseEntry implements AbstractReferencedItemStorage.ReferencableEntry<ApiResponse> {

        private final ApiResponse apiResponse;
        private final Set<String> responseCodes = new HashSet<>();
        private final List<Consumer<ReferenceName>> referenceNameSetters = new ArrayList<>();

        public ReferencableApiResponseEntry withResponseCode(String responseCode) {
            responseCodes.add(responseCode);
            return this;
        }

        @Override
        public ApiResponse getItem() {
            return apiResponse;
        }

        @Override
        public boolean matches(ApiResponse item) {
            return apiResponse.equals(item);
        }

        @Override
        public int getNumberOfUsages() {
            return referenceNameSetters.size();
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return responseCodes.stream().sorted().collect(Collectors.joining("_"));
        }

        @Override
        public Stream<Consumer<ReferenceName>> getReferenceNameSetters() {
            return referenceNameSetters.stream();
        }
    }
}
