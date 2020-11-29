package de.qaware.openapigeneratorforspring.common.reference.component.link;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.model.link.Link;

import java.util.Map;

/**
 * Consumer for to-be-referenced {@link Link links}.
 *
 * @see ReferencedItemConsumerForType
 */
public interface ReferencedLinksConsumer extends ReferencedItemConsumerForType<Map<String, Link>> {

}
