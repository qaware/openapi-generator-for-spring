package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.tag.Tag;

import java.util.List;
import java.util.function.Consumer;

/**
 * Consumer for to-be-referenced {@link Tag tags}. They are always referenced
 * as there is no other way than specifying them within the Open Api model.
 */
public interface ReferencedTagsConsumer extends Consumer<List<Tag>>, ReferencedItemConsumer {

}
