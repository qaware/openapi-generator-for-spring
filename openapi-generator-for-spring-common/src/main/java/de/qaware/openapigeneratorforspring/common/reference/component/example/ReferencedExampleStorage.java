package de.qaware.openapigeneratorforspring.common.reference.component.example;

import de.qaware.openapigeneratorforspring.common.reference.AbstractReferencedItemStorage;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceDeciderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierBuilderForType;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferenceIdentifierConflictResolverForType;
import de.qaware.openapigeneratorforspring.model.example.Example;

import java.util.Collections;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.reference.ReferenceType.EXAMPLE;

public class ReferencedExampleStorage extends AbstractReferencedItemStorage<Example> {

    ReferencedExampleStorage(ReferenceDeciderForType<Example> referenceDecider, ReferenceIdentifierBuilderForType<Example> referenceIdentifierFactory, ReferenceIdentifierConflictResolverForType<Example> referenceIdentifierConflictResolver) {
        super(EXAMPLE, referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver, Example::new,
                Collections.emptyList());
    }

    void maybeReferenceExamples(Map<String, Example> examples) {
        addEntriesFromMap(examples);
    }
}
