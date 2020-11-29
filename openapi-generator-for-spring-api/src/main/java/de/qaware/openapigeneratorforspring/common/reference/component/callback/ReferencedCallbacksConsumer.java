package de.qaware.openapigeneratorforspring.common.reference.component.callback;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.operation.Callback;

import java.util.Map;

/**
 * Consumer for to-be-referenced {@link Callback callbacks}.
 *
 * @see ReferencedItemConsumerForType
 */
public interface ReferencedCallbacksConsumer extends ReferencedItemConsumerForType<Map<String, Callback>> {

}
