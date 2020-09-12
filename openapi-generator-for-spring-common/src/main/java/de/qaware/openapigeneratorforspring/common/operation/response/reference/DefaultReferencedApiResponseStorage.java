package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class DefaultReferencedApiResponseStorage implements ReferencedApiResponseStorage {
    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolverForApiResponse referenceNameConflictResolver;
    private final ReferenceDeciderForApiResponse referenceDecider;

    private final List<ApiResponseWithSetters> entries = new ArrayList<>();

    @Override
    public void storeApiResponseMaybeReference(String responseCode, ApiResponse apiResponse, Consumer<ReferenceName> referenceNameConsumer) {
        getEntryOrAddNew(apiResponse)
                .withResponseCode(responseCode)
                .getReferenceNameSetters().add(referenceNameConsumer);
    }

    @Override
    public Map<String, ApiResponse> buildReferencedApiResponses() {
        Map<String, ApiResponse> referencedApiResponses = new HashMap<>();
        entries.stream()
                .filter(entry -> referenceDecider.turnIntoReference(entry.getApiResponse(), entry.getReferenceNameSetters().size()))
                .collect(Collectors.groupingBy(entry -> referenceNameFactory.create(entry.getApiResponse(), entry.getSuggestedIdentifier())))
                .forEach((referenceName, groupedItemsWithSetters) ->
                        buildUniqueReferenceNames(groupedItemsWithSetters, referenceName).forEach(
                                (uniqueReferenceName, apiResponseWithSetters) -> {
                                    List<Consumer<ReferenceName>> referenceNameSetters = apiResponseWithSetters.getReferenceNameSetters();
                                    ApiResponse apiResponse = apiResponseWithSetters.getApiResponse();
                                    referenceNameSetters.forEach(setter -> setter.accept(uniqueReferenceName));
                                    referencedApiResponses.put(uniqueReferenceName.getIdentifier(), apiResponse);
                                })
                );
        return referencedApiResponses;
    }

    private Map<ReferenceName, ApiResponseWithSetters> buildUniqueReferenceNames(List<ApiResponseWithSetters> itemsWithSetters, ReferenceName referenceName) {
        if (itemsWithSetters.size() == 1) {
            // special case: no conflicts need to be resolved as there's only one item for this reference name
            return Collections.singletonMap(referenceName, itemsWithSetters.get(0));
        }

        List<ApiResponse> itemsWithSameReferenceName = itemsWithSetters.stream()
                .map(ApiResponseWithSetters::getApiResponse)
                .collect(Collectors.toList());

        List<ReferenceName> uniqueReferenceNames = referenceNameConflictResolver.resolveConflict(itemsWithSameReferenceName, referenceName);
        if (uniqueReferenceNames.size() != itemsWithSameReferenceName.size()) {
            throw new IllegalStateException(String.format("Conflict resolver %s did not return expected number %d but %d unique reference names for items referenced by %s",
                    referenceNameConflictResolver.getClass().getSimpleName(), itemsWithSameReferenceName.size(), uniqueReferenceNames.size(), referenceName));
        }

        // zip items into map
        return IntStream.range(0, itemsWithSetters.size()).boxed()
                .collect(Collectors.toMap(
                        uniqueReferenceNames::get,
                        itemsWithSetters::get,
                        (a, b) -> {
                            throw new IllegalStateException(String.format("Found non-unique reference name from conflict resolver %s: %s vs. %s",
                                    referenceNameConflictResolver.getClass().getSimpleName(), a, b));
                        })
                );
    }

    private ApiResponseWithSetters getEntryOrAddNew(ApiResponse apiResponse) {
        return entries.stream()
                .filter(entry -> entry.getApiResponse().equals(apiResponse))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one entry: " + a.apiResponse + " vs. " + b.apiResponse);
                })
                .orElseGet(() -> new ApiResponseWithSetters(apiResponse)); // constructor registers it in entries as well
    }

    @Getter
    private class ApiResponseWithSetters {

        private final ApiResponse apiResponse;
        private final Set<String> responseCodes = new HashSet<>();
        private final List<Consumer<ReferenceName>> referenceNameSetters = new ArrayList<>();

        public ApiResponseWithSetters(ApiResponse apiResponse) {
            this.apiResponse = apiResponse;
            // register instance in parent class
            entries.add(this);
        }

        public ApiResponseWithSetters withResponseCode(String responseCode) {
            responseCodes.add(responseCode);
            return this;
        }

        public String getSuggestedIdentifier() {
            return responseCodes.stream().sorted().collect(Collectors.joining("_"));
        }
    }
}
