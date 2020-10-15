package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.tag.Tag;

import java.util.List;
import java.util.function.Consumer;

public interface ReferencedTagsConsumer extends Consumer<List<Tag>>, ReferencedItemConsumer {

}
