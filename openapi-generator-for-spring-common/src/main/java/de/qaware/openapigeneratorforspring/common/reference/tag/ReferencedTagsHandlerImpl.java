package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.util.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

public class ReferencedTagsHandlerImpl implements ReferencedTagsConsumer, ReferencedItemHandler {

    private final Map<String, Tag> tagsByName = new LinkedHashMap<>();

    public ReferencedTagsHandlerImpl(OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier, TagAnnotationMapper tagAnnotationMapper) {
        openAPIDefinitionAnnotationSupplier.getValues(OpenAPIDefinition::tags)
                .map(tagAnnotationMapper::map)
                .forEach(this::consumeTag);
    }

    @Override
    public void accept(List<Tag> tags) {
        tags.forEach(this::consumeTag);
    }


    @Override
    public void applyToOpenApi(OpenApi openApi) {
        setCollectionIfNotEmpty(new ArrayList<>(tagsByName.values()), openApi::setTags);
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
}
