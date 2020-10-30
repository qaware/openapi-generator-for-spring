package de.qaware.openapigeneratorforspring.common.reference.component.response;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierFactoryForType;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.EXAMPLE;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.HEADER;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.LINK;
import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.SCHEMA;

public class ReferencedApiResponseStorage extends AbstractReferencedItemStorage<ApiResponse, ReferencedApiResponseStorage.Entry> {

    ReferencedApiResponseStorage(ReferenceDeciderForType<ApiResponse> referenceDecider, ReferenceIdentifierFactoryForType<ApiResponse> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<ApiResponse> referenceIdentifierConflictResolver) {
        super(ReferenceType.API_RESPONSE, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, ApiResponse::new, Entry::new,
                Arrays.asList(SCHEMA, EXAMPLE, HEADER, LINK));
    }

    void maybeReferenceApiResponses(ApiResponses apiResponses) {
        // exploit the fact that we've access to the full map of apiResponses
        // that means we can modify the map as a reference
        // if this method is called again for the same owner instance of apiResponses,
        // any previously defined reference consumers need to be removed first
        removeReferenceSettersOwnedBy(apiResponses);
        apiResponses.forEach((responseCode, apiResponse) ->
                getEntryOrAddNew(apiResponse).addSetter(ApiResponseReferenceSetter.of(
                        apiResponseReference -> apiResponses.put(responseCode, apiResponseReference),
                        apiResponses,
                        responseCode
                ))
        );
    }

    static class Entry extends AbstractReferencedItemStorage.AbstractReferencableEntryWithReferenceSetter<ApiResponse, ApiResponseReferenceSetter> {

        protected Entry(ApiResponse item) {
            super(item);
        }

        @Nullable
        @Override
        public String getSuggestedIdentifier() {
            return getReferenceSetters()
                    .map(ApiResponseReferenceSetter::getResponseCode)
                    .distinct()
                    .sorted()
                    .collect(Collectors.joining("_"));
        }
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class ApiResponseReferenceSetter implements AbstractReferencedItemStorage.ReferenceSetter<ApiResponse> {
        private final Consumer<ApiResponse> setter;
        @Getter
        private final Object owner;
        @Getter(AccessLevel.PRIVATE)
        private final String responseCode;

        @Override
        public void consumeReference(ApiResponse referenceItem) {
            setter.accept(referenceItem);
        }
    }
}
