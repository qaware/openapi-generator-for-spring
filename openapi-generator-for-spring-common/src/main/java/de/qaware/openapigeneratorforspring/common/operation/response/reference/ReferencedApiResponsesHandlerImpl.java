package de.qaware.openapigeneratorforspring.common.operation.response.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedApiResponsesHandlerImpl implements ReferencedComponentHandler, ReferencedApiResponsesConsumer {
    private final ReferencedApiResponseStorage storage;

    @Override
    public void maybeAsReference(ApiResponses apiResponses, Consumer<ApiResponses> apiResponsesSetter) {
        apiResponsesSetter.accept(apiResponses);
        storage.maybeReferenceApiResponses(apiResponses);
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setResponses);
    }
}
