/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.tag.ReferencedTagsConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
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
                .toList();
        setCollectionIfNotEmpty(distinctNonBlankTags, operation::setTags);
    }

    private Stream<String> collectTagsFromMethodAndClass(OperationBuilderContext operationBuilderContext) {
        HandlerMethod handlerMethod = operationBuilderContext.getOperationInfo().getHandlerMethod();
        List<Tag> tags = handlerMethod.findAnnotations(io.swagger.v3.oas.annotations.tags.Tag.class)
                .map(tagAnnotationMapper::map)
                .toList();
        setCollectionIfNotEmpty(tags, nonEmptyTags -> operationBuilderContext.getReferencedItemConsumer(ReferencedTagsConsumer.class)
                .accept(nonEmptyTags));
        return tags.stream().map(Tag::getName);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
