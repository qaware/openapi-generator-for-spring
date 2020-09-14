package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedExamplesHandlerImpl implements ReferencedItemHandler<Map<String, Example>>, ReferencedExamplesConsumer {
    private final ReferencedExampleStorage storage;

    @Override
    public void maybeAsReference(Map<String, Example> examples, Consumer<Map<String, Example>> examplesSetter) {
        examplesSetter.accept(examples);
        examples.forEach((name, example) ->
                storage.storeMaybeReference(name, example,
                        referenceName -> examples.put(name, new Example().$ref(referenceName.asReferenceString()))
                )
        );
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setExamples);
    }
}
