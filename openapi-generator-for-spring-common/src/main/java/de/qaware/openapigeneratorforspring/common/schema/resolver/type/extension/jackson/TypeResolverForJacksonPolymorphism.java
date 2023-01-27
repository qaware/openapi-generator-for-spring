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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaPropertiesCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaProperty;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.model.media.Discriminator;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
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
import static de.qaware.openapigeneratorforspring.common.util.OpenApiProxyUtils.createAnnotationProxyWithValueFactory;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStreamUtils.zip;

@RequiredArgsConstructor
@Slf4j
public class TypeResolverForJacksonPolymorphism implements TypeResolver, InitialSchemaBuilder,
        SchemaPropertiesResolver, SchemaPropertiesCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final SchemaNameBuilder schemaNameBuilder;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final OpenApiObjectMapperSupplier objectMapperSupplier;
    private final JacksonPolymorphismTypeSchemaNameBuilder typeSchemaNameBuilder;

    @Nullable
    @Override
    public Schema buildFromType(SchemaResolver.Caller caller, InitialType initialType) {
        return initialType.getAnnotationsSupplier().findAnnotations(JsonTypeInfo.class)
                .reduce((a, b) -> {
                    LOGGER.warn("Found more than one @JsonTypeInfo on {}, will prefer {} over {}", initialType.getType(), a, b);
                    return a;
                })
                .filter(jsonTypeInfo -> {
                    if (StringUtils.isBlank(jsonTypeInfo.use().getDefaultPropertyName())) {
                        LOGGER.debug("Ignoring {} with blank default property name", jsonTypeInfo);
                        return false;
                    }
                    return true;
                })
                .map(jsonTypeInfo -> {
                    String schemaName = schemaNameBuilder.buildFromType(caller, initialType.getType());
                    return new JacksonPolymorphismSchema(schemaName, jsonTypeInfo);
                })
                .orElse(null);
    }

    @Override
    @Nullable
    public RecursionKey resolve(
            SchemaResolver.Caller caller,
            Schema schema,
            InitialType initialType, SchemaBuilderFromType schemaBuilderFromType
    ) {
        JsonTypeInfo jsonTypeInfo = getJsonTypeInfo(schema);
        if (jsonTypeInfo == null) {
            return null;
        }
        JsonTypeInfo.As includeAs = jsonTypeInfo.include();
        if (includeAs != JsonTypeInfo.As.PROPERTY && includeAs != JsonTypeInfo.As.EXISTING_PROPERTY) {
            LOGGER.warn("Encountered unsupported JsonTypeInfo annotation with include = {}, will ignore", jsonTypeInfo.include());
            return null;
        }

        String propertyName = findPropertyName(jsonTypeInfo);
        Class<?> classOwningJsonTypeInfo = findClassOwningJsonTypeInfo(initialType.getType().getRawClass())
                .orElseThrow(() -> new IllegalStateException("Cannot find class/interface annotated with @JsonTypeInfo although the given annotationSupplier detected one"));

        Function<JsonSubTypes.Type, String> typeNameMapper = jsonTypeInfo.use() == MINIMAL_CLASS ?
                type -> findMinimalClassTypeName(type, classOwningJsonTypeInfo) :
                this::findTypeName;

        ObjectMapper objectMapper = objectMapperSupplier.get(SCHEMA_BUILDING);
        String propertySchemaName = getPropertySchemaName(caller, objectMapper, classOwningJsonTypeInfo, jsonTypeInfo);

        setMapIfNotEmpty(buildStringMapFromStream(
                initialType.getAnnotationsSupplier().findAnnotations(JsonSubTypes.class)
                        .flatMap(jsonSubTypes -> Arrays.stream(jsonSubTypes.value())),
                typeNameMapper,
                JsonSubTypes.Type::value
        ), jsonSubTypes -> {

            Map<String, String> schemaReferenceMapping = new LinkedHashMap<>();
            List<Schema> oneOfSchemas = new java.util.ArrayList<>(jsonSubTypes.keySet().stream()
                    .map(ignored -> new Schema())
                    .toList());
            Map<String, Integer> oneOfSchemasIndexMap = zip(
                    jsonSubTypes.keySet().stream(), IntStream.range(0, jsonSubTypes.size()).boxed()
            ).collect(Collectors.toMap(Pair::getKey, Pair::getValue));

            jsonSubTypes.forEach((typeName, type) -> schemaBuilderFromType.buildSchemaReferenceFromType(
                    objectMapper.constructType(type),
                    createAnnotationsSupplier(type, propertyName, jsonSubTypes.keySet(), propertySchemaName, includeAs),
                    schemaReference -> {
                        schemaReferenceMapping.put(typeName, schemaReference.getRef());
                        oneOfSchemas.set(oneOfSchemasIndexMap.get(typeName), schemaReference);
                    }
            ));

            schema.setOneOf(oneOfSchemas);
            schema.setDiscriminator(Discriminator.builder()
                    .propertyName(propertyName)
                    .mapping(schemaReferenceMapping)
                    .build()
            );
        });

        return new UniqueKey(classOwningJsonTypeInfo);
    }

    @Override
    public Map<String, SchemaProperty> findProperties(SchemaResolver.Caller caller, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        return annotationsSupplier.findAnnotations(PropertyIncludeAsAnnotation.class)
                .filter(a -> a.value() == JsonTypeInfo.As.PROPERTY)
                .flatMap(ignored -> annotationsSupplier.findAnnotations(PropertyNameAnnotation.class))
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
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Map<String, ? extends SchemaPropertyCallback> properties) {
        Optional<String> propertyName = annotationsSupplier.findAnnotations(PropertyNameAnnotation.class)
                .findFirst()
                .map(PropertyNameAnnotation::value);
        if (propertyName.isEmpty()) {
            return;
        }
        Optional<List<Object>> propertyEnumValues = annotationsSupplier.findAnnotations(PropertyEnumValuesAnnotation.class)
                .findFirst()
                .map(PropertyEnumValuesAnnotation::value)
                .map(Arrays::asList);
        if (propertyEnumValues.isEmpty()) {
            return;
        }
        Optional<String> propertySchemaName = annotationsSupplier.findAnnotations(PropertySchemaNameAnnotation.class)
                .findFirst()
                .map(PropertySchemaNameAnnotation::value);
        if (propertySchemaName.isEmpty()) {
            return;
        }
        SchemaPropertyCallback schemaPropertyCallback = properties.get(propertyName.get());
        schemaPropertyCallback.customize(
                (propertySchema, propertyJavaType, propertyAnnotationsSupplier) -> {
                    propertySchema.setName(propertySchemaName.get());
                    propertySchema.setEnumValues(propertyEnumValues.get());
                }
        );
    }

    @Nullable
    private JsonTypeInfo getJsonTypeInfo(Schema schema) {
        if (schema instanceof JacksonPolymorphismSchema jacksonPolymorphismSchema) {
            return jacksonPolymorphismSchema.getJsonTypeInfo();
        }
        return null;
    }

    private AnnotationsSupplier createAnnotationsSupplier(Class<?> type, String propertyName, Collection<String> propertyEnumValues, String propertySchemaName, JsonTypeInfo.As includeAs) {
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(type);
        return new AnnotationsSupplier() {
            @Override
            public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                // skipping the JsonTypeInfo when resolving the derived classes
                // avoids ending up in an infinite recursion by running this resolver again and again!
                if (JsonTypeInfo.class.equals(annotationType)) {
                    return Stream.empty();
                } else if (PropertyNameAnnotation.class.equals(annotationType)) {
                    return Stream.of(annotationType.cast(PropertyNameAnnotation.FACTORY.apply(propertyName)));
                } else if (PropertyEnumValuesAnnotation.class.equals(annotationType)) {
                    return Stream.of(annotationType.cast(PropertyEnumValuesAnnotation.FACTORY.apply(propertyEnumValues.toArray(new String[0]))));
                } else if (PropertySchemaNameAnnotation.class.equals(annotationType)) {
                    return Stream.of(annotationType.cast(PropertySchemaNameAnnotation.FACTORY.apply(propertySchemaName)));
                } else if (PropertyIncludeAsAnnotation.class.equals(annotationType)) {
                    return Stream.of(annotationType.cast(PropertyIncludeAsAnnotation.FACTORY.apply(includeAs)));
                }
                return annotationsSupplier.findAnnotations(annotationType);
            }
        };
    }

    private String getPropertySchemaName(SchemaResolver.Caller caller, ObjectMapper objectMapper, Class<?> classOwningJsonTypeInfo, JsonTypeInfo jsonTypeInfo) {
        JavaType javaTypeOwningJsonTypeInfo = objectMapper.constructType(classOwningJsonTypeInfo);
        return typeSchemaNameBuilder.build(caller, javaTypeOwningJsonTypeInfo, jsonTypeInfo);
    }

    private String findTypeName(JsonSubTypes.Type type) {
        // Note: Jackson handles inner non-static classes containing $ actually somewhat different,
        // see com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver._idFrom
        return StringUtils.isNotBlank(type.name()) ? type.name() : type.value().getName();
    }

    private String findMinimalClassTypeName(JsonSubTypes.Type type, Class<?> classOwningJsonTypeInfo) {
        String basePackageName = classOwningJsonTypeInfo.getPackage().getName();
        String typeName = type.value().getName();
        if (typeName.startsWith(basePackageName)) {
            return typeName.substring(basePackageName.length());
        }
        return typeName;
    }

    private String findPropertyName(JsonTypeInfo jsonTypeInfo) {
        String property = jsonTypeInfo.property();
        return StringUtils.isNotBlank(property) ? property : jsonTypeInfo.use().getDefaultPropertyName();
    }

    private static Optional<Class<?>> findClassOwningJsonTypeInfo(Class<?> clazz) {
        if (clazz.isAnnotationPresent(JsonTypeInfo.class)) {
            return Optional.of(clazz);
        }
        return Stream.concat(
                Optional.ofNullable(clazz.getSuperclass())
                        .flatMap(TypeResolverForJacksonPolymorphism::findClassOwningJsonTypeInfo).stream(),
                Arrays.stream(clazz.getInterfaces())
                        .map(TypeResolverForJacksonPolymorphism::findClassOwningJsonTypeInfo)
                        .flatMap(Optional::stream)
        ).findFirst();
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode // important!
    private static class UniqueKey implements RecursionKey {
        private final Class<?> classOwningJsonTypeInfo;
    }

    @EqualsAndHashCode(callSuper = true)
    private static class JacksonPolymorphismSchema extends Schema {
        @Getter
        @EqualsAndHashCode.Exclude
        @JsonIgnore
        private final JsonTypeInfo jsonTypeInfo;

        public JacksonPolymorphismSchema(String name, JsonTypeInfo jsonTypeInfo) {
            this.jsonTypeInfo = jsonTypeInfo;
            setName(name);
        }
    }

    private @interface PropertyNameAnnotation {
        Function<String, PropertyNameAnnotation> FACTORY = createAnnotationProxyWithValueFactory(PropertyNameAnnotation.class, String.class);

        String value();
    }

    private @interface PropertyEnumValuesAnnotation {
        Function<String[], PropertyEnumValuesAnnotation> FACTORY = createAnnotationProxyWithValueFactory(PropertyEnumValuesAnnotation.class, String[].class);

        String[] value();
    }

    private @interface PropertySchemaNameAnnotation {
        Function<String, PropertySchemaNameAnnotation> FACTORY = createAnnotationProxyWithValueFactory(PropertySchemaNameAnnotation.class, String.class);

        String value();
    }

    private @interface PropertyIncludeAsAnnotation {
        Function<JsonTypeInfo.As, PropertyIncludeAsAnnotation> FACTORY = createAnnotationProxyWithValueFactory(PropertyIncludeAsAnnotation.class, JsonTypeInfo.As.class);

        JsonTypeInfo.As value();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
