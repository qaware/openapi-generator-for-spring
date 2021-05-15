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
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.DefaultSchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaProperty;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertyFilter;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils;
import de.qaware.openapigeneratorforspring.model.media.Discriminator;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.MINIMAL_CLASS;
import static de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier.Purpose.SCHEMA_BUILDING;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.takeWhile;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.zip;

@RequiredArgsConstructor
@Slf4j
public class SchemaCustomizerForJacksonPolymorphism implements SchemaCustomizer, SchemaPropertiesResolver,
        SchemaPropertyFilter, SchemaPropertiesCustomizer {

    public static final int ORDER = OpenApiOrderedUtils.earlierThan(DefaultSchemaPropertiesResolver.ORDER);

    private static final String PROPERTY_SCHEMA_TYPE_NAME_SUFFIX = "Type";

    private final SchemaNameBuilder schemaNameBuilder;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final OpenApiObjectMapperSupplier objectMapperSupplier;

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

        String propertyName = findPropertyName(jsonTypeInfo);
        Class<?> classOwningJsonTypeInfo = findClassOwningJsonTypeInfo(javaType.getRawClass())
                .orElseThrow(() -> new IllegalStateException("Cannot find class/interface annotated with @JsonTypeInfo"));

        Function<JsonSubTypes.Type, String> typeNameMapper = jsonTypeInfo.use() == MINIMAL_CLASS ?
                type -> findMinimalClassTypeName(type, classOwningJsonTypeInfo) :
                this::findTypeName;

        setMapIfNotEmpty(buildStringMapFromStream(
                annotationsSupplier.findAnnotations(JsonSubTypes.class)
                        .flatMap(jsonSubTypes -> Arrays.stream(jsonSubTypes.value())),
                typeNameMapper,
                JsonSubTypes.Type::value
        ), jsonSubTypes -> {

            Map<String, String> schemaReferenceMapping = new LinkedHashMap<>();
            List<Schema> oneOfSchemas = jsonSubTypes.keySet().stream()
                    .map(ignored -> new Schema())
                    .collect(Collectors.toList());
            Map<String, Integer> oneOfSchemasIndexMap = zip(
                    jsonSubTypes.keySet().stream(), IntStream.range(0, jsonSubTypes.size()).boxed()
            ).collect(Collectors.toMap(Pair::getKey, Pair::getValue));

            jsonSubTypes.forEach((typeName, type) -> recursiveResolver.alwaysAsReference(type,
                    createAnnotationsSupplier(type, propertyName, jsonSubTypes.keySet(), classOwningJsonTypeInfo),
                    schemaReference -> {
                        schemaReferenceMapping.put(typeName, schemaReference.getRef());
                        oneOfSchemas.set(oneOfSchemasIndexMap.get(typeName), schemaReference);
                    }
            ));

            schema.setType(null); // remove type object from this abstract schema
            schema.setOneOf(oneOfSchemas);
            schema.setDiscriminator(Discriminator.builder()
                    .propertyName(propertyName)
                    .mapping(schemaReferenceMapping)
                    .build()
            );
        });
    }

    private Optional<Class<?>> findClassOwningJsonTypeInfo(Class<?> clazz) {
        if (clazz.isAnnotationPresent(JsonTypeInfo.class)) {
            return Optional.of(clazz);
        }
        return Stream.concat(
                Optional.ofNullable(clazz.getSuperclass())
                        .flatMap(this::findClassOwningJsonTypeInfo)
                        .map(Stream::of).orElseGet(Stream::empty), // Optional.toStream()
                Arrays.stream(clazz.getInterfaces())
                        .map(this::findClassOwningJsonTypeInfo)
                        .flatMap(o -> o.map(Stream::<Class<?>>of).orElseGet(Stream::empty)) // Optional::toStream
        ).findFirst();
    }


    @Override
    public Map<String, SchemaProperty> findProperties(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaResolver.Mode mode) {
        return annotationsSupplier.findAnnotations(PropertyNameAnnotation.class)
                .findFirst()
                .map(propertyNameAnnotation -> Collections.<String, SchemaProperty>singletonMap(
                        propertyNameAnnotation.value(), new SchemaProperty() {
                            @Override
                            public JavaType getType() {
                                return objectMapperSupplier.get(SCHEMA_BUILDING).constructType(String.class);
                            }

                            @Override
                            public AnnotationsSupplier getAnnotationsSupplier() {
                                return AnnotationsSupplier.EMPTY;
                            }
                        }
                ))
                .orElseGet(Collections::emptyMap);
    }

    @Override
    public boolean accept(BeanPropertyDefinition property, BeanDescription beanDescriptionForType, AnnotationsSupplier annotationsSupplierForType, MapperConfig<?> mapperConfig) {
        // this removes any properties found for the "base schema", only keeps the explicitly set fields such as oneOf and discriminator
        return !annotationsSupplierForType.findAnnotations(JsonTypeInfo.class)
                .findFirst()
                .isPresent();
    }

    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Map<String, ? extends SchemaPropertyCallback> properties) {
        Optional<String> propertyName = annotationsSupplier.findAnnotations(PropertyNameAnnotation.class)
                .findFirst()
                .map(PropertyNameAnnotation::value);
        if (!propertyName.isPresent()) {
            return;
        }
        Optional<List<Object>> propertyEnumValues = annotationsSupplier.findAnnotations(PropertyEnumValuesAnnotation.class)
                .findFirst()
                .map(PropertyEnumValuesAnnotation::value)
                .map(Arrays::asList);
        if (!propertyEnumValues.isPresent()) {
            return;
        }
        Optional<String> propertySchemaName = annotationsSupplier.findAnnotations(PropertySchemaNameAnnotation.class)
                .findFirst()
                .map(PropertySchemaNameAnnotation::value);
        if (!propertySchemaName.isPresent()) {
            return;
        }
        properties.get(propertyName.get()).customize(
                (propertySchema, propertyJavaType, propertyAnnotationsSupplier) -> {
                    propertySchema.setName(propertySchemaName.get());
                    propertySchema.setEnumValues(propertyEnumValues.get());
                }
        );
    }

    private AnnotationsSupplier createAnnotationsSupplier(Class<?> type, String propertyName, Collection<String> propertyEnumValues, Class<?> classOwningJsonTypeInfo) {
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(type);
        return new AnnotationsSupplier() {
            @Override
            public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                // skipping the JsonTypeInfo when resolving the derived classes
                // avoids ending up in an infinite recursion by running this customizer again and again!
                if (JsonTypeInfo.class.equals(annotationType)) {
                    return Stream.empty();
                } else if (PropertyNameAnnotation.class.equals(annotationType)) {
                    return Stream.of(createAnnotationProxy(propertyName, annotationType));
                } else if (PropertyEnumValuesAnnotation.class.equals(annotationType)) {
                    return Stream.of(createAnnotationProxy(propertyEnumValues.toArray(new String[]{}), annotationType));
                } else if (PropertySchemaNameAnnotation.class.equals(annotationType)) {
                    return Stream.of(createAnnotationProxy(getPropertySchemaName(classOwningJsonTypeInfo), annotationType));
                }
                return annotationsSupplier.findAnnotations(annotationType);
            }
        };
    }

    private String getPropertySchemaName(Class<?> classOwningJsonTypeInfo) {
        JavaType javaTypeOwningJsonTypeInfo = objectMapperSupplier.get(SCHEMA_BUILDING).constructType(classOwningJsonTypeInfo);
        return schemaNameBuilder.buildFromType(javaTypeOwningJsonTypeInfo) + PROPERTY_SCHEMA_TYPE_NAME_SUFFIX;
    }

    private String findTypeName(JsonSubTypes.Type type) {
        // Note: Jackson handles inner non-static classes containing $ actually somewhat different,
        // see com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver._idFrom
        return StringUtils.isNotBlank(type.name()) ? type.name() : type.value().getName();
    }

    private String findMinimalClassTypeName(JsonSubTypes.Type type, Class<?> classOwningJsonTypeInfo) {
        String typeName = type.value().getName();
        String owningJsonTypeInfoName = classOwningJsonTypeInfo.getName();
        int stripIndex = findCommonBaseNameStripIndex(Arrays.asList(owningJsonTypeInfoName, typeName));
        return typeName.substring(stripIndex);
    }

    private String findPropertyName(JsonTypeInfo jsonTypeInfo) {
        String property = jsonTypeInfo.property();
        return StringUtils.isNotBlank(property) ? property : jsonTypeInfo.use().getDefaultPropertyName();
    }

    static int findCommonBaseNameStripIndex(Collection<String> typeNames) {
        return typeNames.stream()
                .map(typeName -> Arrays.stream(typeName.split("(?<=\\.)"))
                        .filter(s -> s.endsWith("."))
                )
                .reduce((a, b) ->
                        takeWhile(zip(a, b), p -> p.getRight().equals(p.getLeft()))
                                .map(Pair::getRight)
                )
                .flatMap(OpenApiStreamUtils::nonEmptyStream)
                .map(s -> s.mapToInt(String::length).sum() - 1)
                .orElse(0);
    }

    private @interface PropertyNameAnnotation {
        String value();
    }

    private @interface PropertyEnumValuesAnnotation {
        String[] value();
    }

    private @interface PropertySchemaNameAnnotation {
        String value();
    }

    private static <A extends Annotation> A createAnnotationProxy(Object value, Class<A> clazz) {
        return clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                (proxy, method, args) -> {
                    if (method.getName().equals("value")) {
                        return value;
                    }
                    return method.invoke(proxy, args);
                }
        ));
    }


    @Override
    public int getOrder() {
        return ORDER;
    }
}
