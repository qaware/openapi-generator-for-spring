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

package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferencedExamplesConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultContentAnnotationMapper implements ContentAnnotationMapper {

    private final EncodingAnnotationMapper encodingAnnotationMapper;
    private final SchemaAnnotationMapper schemaAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final ExampleObjectAnnotationMapper exampleObjectAnnotationMapper;

    @Override
    public Content mapArray(io.swagger.v3.oas.annotations.media.Content[] contentAnnotations, Class<? extends HasContent> owningType, SchemaResolver.Caller caller, MapperContext mapperContext) {
        return Arrays.stream(contentAnnotations)
                .flatMap(contentAnnotation -> {
                    MediaType mediaTypeValue = map(caller, contentAnnotation, mapperContext);
                    if (StringUtils.isBlank(contentAnnotation.mediaType())) {
                        // if the mapperContext doesn't have any suggested media types,
                        // the mediaTypeValue is discarded!
                        Set<String> mediaTypes = mapperContext.findMediaTypes(owningType)
                                .orElseThrow(() -> new IllegalStateException("No media types available in context for " + owningType.getSimpleName()
                                        + " and Content annotation has blank mediaType"));
                        return mediaTypes.stream().map(mediaType -> Pair.of(mediaType, mediaTypeValue));
                    }
                    return Stream.of(Pair.of(contentAnnotation.mediaType(), mediaTypeValue));
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> {
                    if (!Objects.equals(a, b)) {
                        throw new IllegalStateException("Conflicting media type found for " + a + " vs. " + b);
                    }
                    return a;
                }, Content::new));
    }

    private MediaType map(SchemaResolver.Caller caller, io.swagger.v3.oas.annotations.media.Content contentAnnotation, MapperContext mapperContext) {
        MediaType mediaType = new MediaType();
        setExampleOrExamples(mediaType, contentAnnotation.examples(), mapperContext);
        setMapIfNotEmpty(encodingAnnotationMapper.mapArray(contentAnnotation.encoding(), mapperContext), mediaType::setEncoding);
        ReferencedSchemaConsumer referencedSchemaConsumer = mapperContext.getReferencedItemConsumer(ReferencedSchemaConsumer.class);
        setIfNotEmpty(schemaAnnotationMapper.buildFromAnnotation(caller, contentAnnotation.schema(), referencedSchemaConsumer),
                schema -> referencedSchemaConsumer.maybeAsReference(schema, mediaType::setSchema)
        );
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(contentAnnotation.extensions()), mediaType::setExtensions);
        return mediaType;
    }

    private void setExampleOrExamples(MediaType mediaType, ExampleObject[] exampleObjectAnnotations, MapperContext mapperContext) {
        if (exampleObjectAnnotations.length == 1 && StringUtils.isBlank(exampleObjectAnnotations[0].name())) {
            setIfNotEmpty(exampleObjectAnnotationMapper.map(exampleObjectAnnotations[0]),
                    // one should not set the full example object here, just the value
                    // so no referencing is needed in this case
                    example -> mediaType.setExample(example.getValue())
            );
        } else {
            ReferencedExamplesConsumer referencedExamplesConsumer = mapperContext.getReferencedItemConsumer(ReferencedExamplesConsumer.class);
            setMapIfNotEmpty(exampleObjectAnnotationMapper.mapArray(exampleObjectAnnotations),
                    examples -> referencedExamplesConsumer.maybeAsReference(examples, mediaType::setExamples)
            );
        }
    }
}
