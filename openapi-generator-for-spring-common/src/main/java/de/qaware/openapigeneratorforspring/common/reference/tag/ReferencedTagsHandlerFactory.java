package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationAnnotationsSupplier;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;

@RequiredArgsConstructor
public class ReferencedTagsHandlerFactory implements ReferencedItemHandlerFactory {

    private final OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier;
    private final TagAnnotationMapper tagAnnotationMapper;

    @Override
    public ReferencedItemHandler create() {
        Map<String, de.qaware.openapigeneratorforspring.model.tag.Tag> tagsByName = buildStringMapFromStream(
                springBootApplicationAnnotationsSupplier.findFirstAnnotation(OpenAPIDefinition.class)
                        .map(OpenAPIDefinition::tags)
                        .map(Arrays::stream)
                        .orElseGet(Stream::empty),
                io.swagger.v3.oas.annotations.tags.Tag::name,
                tagAnnotationMapper::map
        );
        return new ReferencedTagsHandlerImpl(tagsByName);
    }
}
