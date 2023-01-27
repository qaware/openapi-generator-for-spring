/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 - 2021 QAware GmbH
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

package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.model.media.Discriminator;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class SchemaCustomizerForSchemaAnnotation implements SchemaCustomizer {

    private final ParsableValueMapper parsableValueMapper;
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveResolver recursiveResolver) {
        annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                // apply in reverse order
                .collect(Collectors.toCollection(LinkedList::new))
                .descendingIterator()
                .forEachRemaining(schemaAnnotation -> applyFromAnnotation(schema, schemaAnnotation, recursiveResolver));
    }

    public void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.Schema annotation, RecursiveResolver recursiveResolver) {

        setStringIfNotBlank(annotation.name(), schema::setName);
        setStringIfNotBlank(annotation.title(), schema::setTitle);
        setStringIfNotBlank(annotation.description(), schema::setDescription);
        setStringIfNotBlank(annotation.format(), schema::setFormat);
        setStringIfNotBlank(annotation.ref(), schema::setRef);

        if (annotation.nullable()) {
            schema.setNullable(true);
        }
        setCollectionIfNotEmpty(Arrays.asList(annotation.requiredProperties()), schema::setRequired);
        setAccessMode(annotation.accessMode(), schema);
        setStringIfNotBlank(annotation.example(), example -> schema.setExample(parsableValueMapper.parse(example)));
        setIfNotEmpty(externalDocumentationAnnotationMapper.map(annotation.externalDocs()), schema::setExternalDocs);
        if (annotation.deprecated()) {
            schema.setDeprecated(true);
        }
        setStringIfNotBlank(annotation.type(), schema::setType);

        List<Object> allowableValues = Stream.of(annotation.allowableValues())
                .map(parsableValueMapper::parse)
                .toList();
        setCollectionIfNotEmpty(allowableValues, schema::setEnumValues);

        setStringIfNotBlank(annotation.defaultValue(), value -> schema.setDefaultValue(parsableValueMapper.parse(value)));

        setDiscriminator(schema, annotation, recursiveResolver);

        mergeWithExistingMap(schema::getExtensions, schema::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
    }

    private void setDiscriminator(Schema schema, io.swagger.v3.oas.annotations.media.Schema annotation, RecursiveResolver recursiveResolver) {
        String propertyName = annotation.discriminatorProperty();
        DiscriminatorMapping[] mappings = annotation.discriminatorMapping();
        if (StringUtils.isBlank(propertyName) || ArrayUtils.isEmpty(mappings)) {
            return;
        }

        Map<String, String> schemaReferenceMapping = new LinkedHashMap<>();
        schema.setDiscriminator(Discriminator.builder()
                .propertyName(propertyName)
                .mapping(schemaReferenceMapping)
                .build()
        );

        for (DiscriminatorMapping mapping : mappings) {
            recursiveResolver.alwaysAsReference(mapping.schema(), schemaReference -> schemaReferenceMapping.put(mapping.value(), schemaReference.getRef()));
        }
    }

    private void setAccessMode(io.swagger.v3.oas.annotations.media.Schema.AccessMode accessMode, Schema schema) {
        switch (accessMode) {
            case READ_ONLY:
                schema.setReadOnly(true);
                break;
            case WRITE_ONLY:
                schema.setWriteOnly(true);
                break;
            case READ_WRITE, AUTO:
                break;
        }
    }
}
