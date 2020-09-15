package de.qaware.openapigeneratorforspring.common.reference.example;

import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import io.swagger.v3.oas.models.examples.Example;

import java.util.List;
import java.util.Map;

public interface ReferencedExamplesConsumer extends ReferencedItemConsumerForType<List<ExampleObjectAnnotationMapper.ExampleWithOptionalName>, Map<String, Example>> {

}
