package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.example.Example;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedExamplesHandlerImpl implements
        ReferencedItemHandler<List<ExampleObjectAnnotationMapper.ExampleWithOptionalName>, Map<String, Example>>,
        ReferencedExamplesConsumer {

    private final ReferencedExampleStorage storage;
    private final ReferenceIdentifierFactoryForExample referenceIdentifierFactory;

    @Override
    public void maybeAsReference(List<ExampleObjectAnnotationMapper.ExampleWithOptionalName> examples, Consumer<Map<String, Example>> examplesSetter) {
        // if the provided example doesn't have a name,
        // we use the suggested identifier from the factory
        Map<String, Example> examplesMap = examples.stream()
                .map(exampleWithOptionalName -> Pair.of(
                        getExampleName(exampleWithOptionalName.getName()),
                        exampleWithOptionalName.getExample()
                ))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> {
                    throw new IllegalStateException("Non-unique suggested identifier encountered: " + a + " vs. " + b);
                }));
        examplesSetter.accept(examplesMap);
        examplesMap.forEach((name, example) ->
                storage.storeMaybeReference(name, example,
                        referenceIdentifier -> examplesMap.put(name, Example.builder().ref(referenceIdentifier).build())
                )
        );
    }

    private String getExampleName(@Nullable String exampleName) {
        return referenceIdentifierFactory.buildSuggestedIdentifier(exampleName);
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setExamples);
    }
}
