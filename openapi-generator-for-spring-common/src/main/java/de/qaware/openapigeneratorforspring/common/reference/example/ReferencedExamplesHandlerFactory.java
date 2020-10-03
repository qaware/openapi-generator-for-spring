package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper.ExampleWithOptionalName;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.example.Example;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ReferencedExamplesHandlerFactory implements ReferencedItemHandlerFactory<List<ExampleWithOptionalName>, Map<String, Example>> {
    private final ReferenceDeciderForExample referenceDecider;
    private final ReferenceIdentifierFactoryForExample referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForExample referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler<List<ExampleWithOptionalName>, Map<String, Example>> create() {
        ReferencedExampleStorage storage = new ReferencedExampleStorage(
                referenceDecider,
                (example, exampleName) -> Collections.singletonList(referenceIdentifierFactory.buildSuggestedIdentifier(exampleName)),
                referenceIdentifierConflictResolver
        );
        return new ReferencedExamplesHandlerImpl(storage, referenceIdentifierFactory);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forClassWithGenerics(List.class, ExampleWithOptionalName.class);
    }
}