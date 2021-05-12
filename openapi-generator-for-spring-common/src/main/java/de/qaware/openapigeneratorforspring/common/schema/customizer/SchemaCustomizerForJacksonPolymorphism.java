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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.model.media.Discriminator;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.MINIMAL_CLASS;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.takeWhile;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.zip;

@RequiredArgsConstructor
@Slf4j
public class SchemaCustomizerForJacksonPolymorphism implements SchemaCustomizer {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveResolver recursiveResolver) {
        JsonTypeInfo jsonTypeInfo = annotationsSupplier.findAnnotations(JsonTypeInfo.class)
                .reduce((a, b) -> {
                    LOGGER.warn("Found more than one @JsonTypeInfo on {}, will prefer {} over {}", javaType, a, b);
                    return a;
                })
                .orElse(null);

        if (jsonTypeInfo == null || StringUtils.isBlank(jsonTypeInfo.use().getDefaultPropertyName())) {
            return;
        }

        setMapIfNotEmpty(buildStringMapFromStream(
                annotationsSupplier.findAnnotations(JsonSubTypes.class)
                        .flatMap(jsonSubTypes -> Arrays.stream(jsonSubTypes.value())),
                this::findTypeName,
                JsonSubTypes.Type::value
        ), jsonSubTypes -> {

            Map<String, String> schemaReferenceMapping = new LinkedHashMap<>();

            // TODO inferring the minimal class type name is not correct here
            // it should take into account the base class where @JsonTypeInfo is located
            // see com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver
            int stripIndex = jsonTypeInfo.use() == MINIMAL_CLASS ? findCommonBaseNameStripIndex(jsonSubTypes.keySet()) : 0;
            jsonSubTypes.forEach((typeName, type) -> recursiveResolver.alwaysAsReference(type, createAnnotationsSupplier(type),
                    schemaReference -> schemaReferenceMapping.put(typeName.substring(stripIndex), schemaReference)
            ));

            schema.setDiscriminator(Discriminator.builder()
                    .propertyName(findPropertyName(jsonTypeInfo))
                    .mapping(schemaReferenceMapping)
                    .build()
            );
        });
    }

    static int findCommonBaseNameStripIndex(Collection<String> typeNames) {
        return typeNames.stream()
                .map(typeName -> Arrays.stream(typeName.split("(?<=\\.)")))
                .reduce((a, b) ->
                        takeWhile(zip(a, b, Pair::of), p -> p.getRight().equals(p.getLeft()))
                                .map(Pair::getRight))
                .map(s -> s.collect(Collectors.toList()))
                .filter(list -> !list.isEmpty())
                .map(s -> s.stream().mapToInt(String::length).sum() - 1)
                .orElse(0);
    }

    private AnnotationsSupplier createAnnotationsSupplier(Class<?> type) {
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(type);
        return new AnnotationsSupplier() {
            @Override
            public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                // skipping the JsonTypeInfo when resolving the derived classes
                // avoids ending up in an infinite recursion by running this customizer again and again!
                if (JsonTypeInfo.class.equals(annotationType)) {
                    return Stream.empty();
                }
                return annotationsSupplier.findAnnotations(annotationType);
            }
        };
    }

    private String findTypeName(JsonSubTypes.Type type) {
        // TODO Jackson handles inner non-static classes containing $ actually somewhat different,
        // see com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver._idFrom
        return StringUtils.isNotBlank(type.name()) ? type.name() : type.value().getName();
    }

    private String findPropertyName(JsonTypeInfo jsonTypeInfo) {
        String property = jsonTypeInfo.property();
        return StringUtils.isNotBlank(property) ? property : jsonTypeInfo.use().getDefaultPropertyName();
    }
}
