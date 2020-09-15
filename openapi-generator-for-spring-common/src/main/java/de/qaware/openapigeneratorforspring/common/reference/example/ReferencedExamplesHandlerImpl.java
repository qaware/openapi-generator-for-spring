package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;
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
    private final ReferenceNameFactoryForExample referenceNameFactory;

    @Override
    public void maybeAsReference(List<ExampleObjectAnnotationMapper.ExampleWithOptionalName> examples, Consumer<Map<String, Example>> examplesSetter) {
        // if the provided example doesn't have a name,
        // we use the suggested identifier from the reference name factory
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
                        referenceName -> examplesMap.put(name, new Example().$ref(referenceName.asReferenceString()))
                )
        );
    }

    private String getExampleName(@Nullable String exampleName) {
        return referenceNameFactory.buildSuggestedIdentifier(exampleName);
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(storage.buildReferencedItems(), components::setExamples);
    }
}
