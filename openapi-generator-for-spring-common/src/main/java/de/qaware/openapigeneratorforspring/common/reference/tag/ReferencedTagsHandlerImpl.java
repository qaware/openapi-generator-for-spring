package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

public class ReferencedTagsHandlerImpl implements ReferencedTagsConsumer, ReferencedItemHandler {

    private final Map<String, Tag> tagsByName = new HashMap<>();

    public static Tag mergeTag(Tag a, Tag b) {
        if (a.withName(null).isEmpty()) {
            return b;
        }
        if (b.withName(null).isEmpty() || a.equals(b)) {
            return a;
        }
        throw new IllegalStateException("Found conflicting tag with identical name: " + a + " vs. " + b);
    }

    @Override
    public void accept(List<Tag> tags) {
        tags.forEach(this::consumeTag);
    }

    private void consumeTag(Tag tag) {
        if (StringUtils.isBlank(tag.getName())) {
            throw new IllegalStateException("Tag has blank name: " + tag);
        }
        tagsByName.merge(tag.getName(), tag, ReferencedTagsHandlerImpl::mergeTag);
    }

    @Override
    public void applyToOpenApi(OpenApi openApi) {
        applyToOpenApi(tagsByName, openApi::setTags);
    }

    public static void applyToOpenApi(Map<String, Tag> tagsByName, Consumer<List<Tag>> tagsSetter) {
        setCollectionIfNotEmpty(new ArrayList<>(tagsByName.values()), tagsSetter);
    }
}
