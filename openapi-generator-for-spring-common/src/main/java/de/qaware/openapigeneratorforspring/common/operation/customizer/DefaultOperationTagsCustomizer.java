package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
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
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        setTagNamesToOperation(operation, collectTagsFromMethodAndClass(operationBuilderContext));
    }

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        operationBuilderContext.getTagsConsumer().accept(
                // also consume tag names only, even if no description or additional information is present
                // the tags consumer is smart enough to pick the right tag model on identical names
                Stream.of(operationAnnotation.tags())
                        .map(tagName -> new Tag().withName(tagName))
                        .collect(Collectors.toList())
        );
        setTagNamesToOperation(operation, Stream.concat(collectTagsFromMethodAndClass(operationBuilderContext),
                Stream.of(operationAnnotation.tags())));
    }

    private static void setTagNamesToOperation(Operation operation, Stream<String> tagNames) {
        List<String> distinctNonBlankTags = tagNames
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        setCollectionIfNotEmpty(distinctNonBlankTags, operation::setTags);
    }

    private Stream<String> collectTagsFromMethodAndClass(OperationBuilderContext operationBuilderContext) {
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromMethodWithDeclaringClass(operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod());
        List<Tag> tags = Stream.concat(
                annotationsSupplier.findAnnotations(Tags.class).flatMap(tagsAnnotation -> Stream.of(tagsAnnotation.value())),
                annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.tags.Tag.class)
        )
                .map(tagAnnotationMapper::map)
                .collect(Collectors.toList());
        operationBuilderContext.getTagsConsumer().accept(tags);
        return tags.stream().map(Tag::getName);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
