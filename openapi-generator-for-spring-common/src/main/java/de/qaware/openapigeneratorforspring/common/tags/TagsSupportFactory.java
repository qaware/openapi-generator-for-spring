package de.qaware.openapigeneratorforspring.common.tags;

import de.qaware.openapigeneratorforspring.model.tag.Tag;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TagsSupportFactory {

    public TagsSupport create() {

        Map<String, Tag> tagsByName = new LinkedHashMap<>();

        return new TagsSupport() {
            @Override
            public Consumer<List<Tag>> getTagsConsumer() {
                return tags -> tags.forEach(tag -> {
                    if (StringUtils.isBlank(tag.getName())) {
                        throw new IllegalStateException("Tag has blank name: " + tag);
                    }
                    tagsByName.merge(tag.getName(), tag, (a, b) -> {
                        if (a.withName(null).isEmpty()) {
                            return b;
                        }
                        if (b.withName(null).isEmpty() || a.equals(b)) {
                            return a;
                        }
                        throw new IllegalStateException("Found conflicting tag with identical name: " + a + " vs. " + b);
                    });
                });
            }

            @Nullable
            @Override
            public List<Tag> buildTags() {
                if (tagsByName.isEmpty()) {
                    return null;
                }
                return new ArrayList<>(tagsByName.values());
            }
        };
    }
}
