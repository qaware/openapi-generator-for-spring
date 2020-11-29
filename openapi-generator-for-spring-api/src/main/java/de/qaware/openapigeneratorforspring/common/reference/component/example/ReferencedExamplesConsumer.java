package de.qaware.openapigeneratorforspring.common.reference.component.example;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.example.Example;

import java.util.Map;

/**
 * Consumer for to-be-referenced {@link Example examples}.
 *
 * @see ReferencedItemConsumerForType
 */
public interface ReferencedExamplesConsumer extends ReferencedItemConsumerForType<Map<String, Example>> {
}
