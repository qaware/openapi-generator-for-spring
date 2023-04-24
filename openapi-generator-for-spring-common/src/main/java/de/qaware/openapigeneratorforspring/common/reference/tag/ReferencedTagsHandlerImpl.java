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

package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemBuildContext;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.supplier.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

public class ReferencedTagsHandlerImpl implements ReferencedTagsConsumer, ReferencedItemHandler {

    private final Map<String, Tag> tagsByName = new LinkedHashMap<>();

    public ReferencedTagsHandlerImpl(OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier, TagAnnotationMapper tagAnnotationMapper) {
        openAPIDefinitionAnnotationSupplier.getAnnotations(OpenAPIDefinition::tags)
                .map(tagAnnotationMapper::map)
                .forEach(this::consumeTag);
    }

    @Override
    public void accept(List<Tag> tags) {
        tags.forEach(this::consumeTag);
    }


    @Override
    public void applyToOpenApi(OpenApi openApi, @Nullable ReferencedItemBuildContext context) {
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
