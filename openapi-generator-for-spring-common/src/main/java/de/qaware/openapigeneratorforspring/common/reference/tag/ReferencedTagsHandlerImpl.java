package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

@RequiredArgsConstructor
public class ReferencedTagsHandlerImpl implements ReferencedTagsConsumer, ReferencedItemHandler {

    private final Map<String, Tag> tagsByName;

    @Override
    public void accept(List<Tag> tags) {
        tags.forEach(this::consumeTag);
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

    @Override
    public void applyToOpenApi(OpenApi openApi) {
        setCollectionIfNotEmpty(new ArrayList<>(tagsByName.values()), openApi::setTags);
    }
}
