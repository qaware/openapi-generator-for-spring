package de.qaware.openapigeneratorforspring.common.reference.component.parameter;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import java.util.List;

/**
 * Consumer for to-be-referenced {@link Parameter parameters}. Additionally
 * requires an owner.
 *
 * @see ReferencedItemConsumerForType
 */
public interface ReferencedParametersConsumer extends ReferencedItemConsumerForType<List<Parameter>> {
    ReferencedParametersConsumer withOwner(Object owner);
}
