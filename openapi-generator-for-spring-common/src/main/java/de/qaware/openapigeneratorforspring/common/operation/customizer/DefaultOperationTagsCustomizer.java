package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.reference.tag.ReferencedTagsConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationTagsCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final TagAnnotationMapper tagAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        setTagNamesToOperation(operation, Stream.concat(
                operation.getTags() == null ? Stream.empty() : operation.getTags().stream(),
                collectTagsFromMethodAndClass(operationBuilderContext)
        ));
    }

    private static void setTagNamesToOperation(Operation operation, Stream<String> tagNames) {
        List<String> distinctNonBlankTags = tagNames
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        setCollectionIfNotEmpty(distinctNonBlankTags, operation::setTags);
    }

    private Stream<String> collectTagsFromMethodAndClass(OperationBuilderContext operationBuilderContext) {
        AnnotationsSupplier annotationsSupplier = operationBuilderContext.getOperationInfo().getHandlerMethod().getAnnotationsSupplier();
        List<Tag> tags = annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.tags.Tag.class)
                .map(tagAnnotationMapper::map)
                .collect(Collectors.toList());
        setCollectionIfNotEmpty(tags, nonEmptyTags -> operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedTagsConsumer.class).accept(nonEmptyTags));
        return tags.stream().map(Tag::getName);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
