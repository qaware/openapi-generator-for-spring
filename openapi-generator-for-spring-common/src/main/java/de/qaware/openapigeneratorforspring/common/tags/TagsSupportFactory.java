package de.qaware.openapigeneratorforspring.common.tags;

import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;

@RequiredArgsConstructor
public class TagsSupportFactory {

    private final TagAnnotationMapper tagAnnotationMapper;

    public TagsSupport create(Stream<io.swagger.v3.oas.annotations.tags.Tag> tagAnnotations) {

        Map<String, Tag> tagsByName = buildStringMapFromStream(
                tagAnnotations,
                io.swagger.v3.oas.annotations.tags.Tag::name,
                tagAnnotationMapper::map
        );

        return new TagsSupport() {
            @Override
            public Consumer<List<Tag>> getTagsConsumer() {
                return tags -> tags.forEach(this::consumeTag);
            }

            private void consumeTag(Tag tag) {
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
