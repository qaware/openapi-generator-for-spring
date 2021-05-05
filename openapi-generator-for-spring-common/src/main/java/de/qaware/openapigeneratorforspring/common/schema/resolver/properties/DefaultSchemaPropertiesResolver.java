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

package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier.Purpose.SCHEMA_BUILDING;

@RequiredArgsConstructor
@Slf4j
public class DefaultSchemaPropertiesResolver implements SchemaPropertiesResolver {

    private final OpenApiObjectMapperSupplier objectMapperSupplier;
    private final List<SchemaPropertyFilter> propertyFilters;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public Map<String, SchemaProperty> findProperties(JavaType javaType, SchemaResolver.Mode mode) {
        ObjectMapper objectMapper = objectMapperSupplier.get(SCHEMA_BUILDING);
        BeanDescription beanDescriptionForType = introspectJavaType(objectMapper, mode, javaType);
        MapperConfig<?> mapperConfig = getMapperConfig(objectMapper, mode);
        return OpenApiMapUtils.buildStringMapFromStream(
                beanDescriptionForType.findProperties().stream()
                        .filter(property -> isAcceptedByAllPropertyFilters(property, beanDescriptionForType, mapperConfig))
                        .map(property -> buildSchemaPropertyPair(property, mode))
                        .filter(Objects::nonNull),
                Pair::getKey,
                Pair::getValue
        );
    }

    @Nullable
    private Pair<String, SchemaProperty> buildSchemaPropertyPair(BeanPropertyDefinition property, SchemaResolver.Mode mode) {
        return buildAnnotationsSupplierFromAnnotatedMembers(property, mode)
                .map(annotationsSupplier -> {
                    JavaType propertyType = property.getPrimaryType();
                    // include more annotations from the actual type of the property
                    AnnotationsSupplier propertyAnnotationsSupplier = annotationsSupplier
                            .andThen(annotationsSupplierFactory.createFromAnnotatedElement(propertyType.getRawClass()));
                    return Pair.<String, SchemaProperty>of(property.getName(),
                            SchemaPropertyImpl.of(propertyType, propertyAnnotationsSupplier)
                    );
                })
                .orElseGet(() -> {
                    LOGGER.debug("Ignoring property without any annotated members for mode {}: {}", mode, property);
                    return null;
                });
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class SchemaPropertyImpl implements SchemaPropertiesResolver.SchemaProperty {
        private final JavaType type;
        private final AnnotationsSupplier annotationsSupplier;
    }


    private Optional<AnnotationsSupplier> buildAnnotationsSupplierFromAnnotatedMembers(BeanPropertyDefinition property, SchemaResolver.Mode mode) {
        // follows the algorithm of com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder.getPrimaryType
        // but enables finding annotations from any place (and not just finding a type)
        // if no annotated members are found, that property should be ignored for serialization/deserialization
        Stream<Optional<AnnotatedMember>> stream;
        if (mode == SchemaResolver.Mode.FOR_DESERIALIZATION) {
            AnnotatedMethod setter = property.getSetter();
            AnnotatedMember setterParameter = setter != null && setter.getParameterCount() == 1 ? setter.getParameter(0) : null;
            stream = Stream.of(
                    // the earlier the annotated member is in this stream, the higher is it's precedence when finding annotations
                    Optional.ofNullable(property.getConstructorParameter()),
                    Optional.ofNullable(setter),
                    Optional.ofNullable(setterParameter),
                    Optional.ofNullable(property.getField())
            );
        } else if (mode == SchemaResolver.Mode.FOR_SERIALIZATION) {
            stream = Stream.of(
                    // the earlier the annotated member is in this stream, the higher is it's precedence when finding annotations
                    Optional.ofNullable(property.getGetter()),
                    Optional.ofNullable(property.getField())
            );
        } else {
            throw new IllegalStateException("Unknown schema resolver mode " + mode);
        }
        return stream
                .flatMap(annotationMember -> annotationMember.map(Stream::of).orElseGet(Stream::empty))
                .map(annotationsSupplierFactory::createFromMember)
                .reduce(AnnotationsSupplier::andThen);
    }

    private static MapperConfig<?> getMapperConfig(ObjectMapper objectMapper, SchemaResolver.Mode mode) {
        if (mode == SchemaResolver.Mode.FOR_DESERIALIZATION) {
            return objectMapper.getDeserializationConfig();
        } else if (mode == SchemaResolver.Mode.FOR_SERIALIZATION) {
            return objectMapper.getSerializationConfig();
        }
        throw new IllegalStateException("Cannot find mapper config for schema resolver mode " + mode);
    }

    private static BeanDescription introspectJavaType(ObjectMapper objectMapper, SchemaResolver.Mode mode, JavaType javaType) {
        if (mode == SchemaResolver.Mode.FOR_DESERIALIZATION) {
            return objectMapper.getDeserializationConfig().introspect(javaType);
        } else if (mode == SchemaResolver.Mode.FOR_SERIALIZATION) {
            return objectMapper.getSerializationConfig().introspect(javaType);
        } else {
            throw new IllegalStateException("Unknown schema resolver mode " + mode);
        }
    }

    private boolean isAcceptedByAllPropertyFilters(BeanPropertyDefinition property, BeanDescription beanDescriptionForType, MapperConfig<?> mapperConfig) {
        return propertyFilters.stream().allMatch(filter -> filter.accept(property, beanDescriptionForType, mapperConfig));
    }
}
