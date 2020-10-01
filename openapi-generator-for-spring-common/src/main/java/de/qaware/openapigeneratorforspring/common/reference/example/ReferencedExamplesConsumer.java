package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.example.Example;

import java.util.List;
import java.util.Map;

public interface ReferencedExamplesConsumer extends ReferencedItemConsumerForType<List<ExampleObjectAnnotationMapper.ExampleWithOptionalName>, Map<String, Example>> {

}
