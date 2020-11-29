package de.qaware.openapigeneratorforspring.common.reference.component.header;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.header.Header;

import java.util.Map;

/**
 * Consumer for to-be-referenced {@link Header headers}.
 *
 * @see ReferencedItemConsumerForType
 */
public interface ReferencedHeadersConsumer extends ReferencedItemConsumerForType<Map<String, Header>> {

}
