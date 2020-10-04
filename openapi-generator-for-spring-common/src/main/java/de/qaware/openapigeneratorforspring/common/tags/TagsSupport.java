package de.qaware.openapigeneratorforspring.common.tags;

import de.qaware.openapigeneratorforspring.model.tag.Tag;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public interface TagsSupport {
    Consumer<List<Tag>> getTagsConsumer();

    @Nullable
    List<Tag> buildTags();
}
