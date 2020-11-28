package de.qaware.openapigeneratorforspring.common.reference.component.example;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.handler.DependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.example.Example;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ReferencedExamplesHandlerImpl implements DependentReferencedComponentHandler, ReferencedExamplesConsumer {

    private final ReferencedExampleStorage storage;

    @Override
    public void maybeAsReference(Map<String, Example> examples, Consumer<Map<String, Example>> examplesSetter) {
        examplesSetter.accept(examples);
        storage.maybeReferenceExamples(examples);
    }

    @Override
    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return storage.getBuildDependencies();
    }

    @Override
    public void applyToComponents(Components components) {
        storage.buildReferencedItems(components::setExamples);
    }

}
