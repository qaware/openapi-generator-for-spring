package de.qaware.openapigeneratorforspring.common.reference.component.response;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.handler.DependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedApiResponsesHandlerImpl implements DependentReferencedComponentHandler, ReferencedApiResponsesConsumer {
    private final ReferencedApiResponseStorage storage;

    @Override
    public void maybeAsReference(ApiResponses apiResponses, Consumer<ApiResponses> apiResponsesSetter) {
        apiResponsesSetter.accept(apiResponses);
        storage.maybeReferenceApiResponses(apiResponses);
    }

    @Override
    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return storage.getBuildDependencies();
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setResponses);
    }
}
