package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS;
import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format.INFER_FROM_OBJECT_MAPPER;
import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format.ISO8601;
import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format.UNIX_EPOCH_SECONDS;

@RequiredArgsConstructor
public class DefaultJava8TimeInitialSchemaBuilder implements Java8TimeInitialSchemaBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    private final Java8TimeTypeResolverConfigurationProperties properties;
    @Nullable
    private final ObjectMapper objectMapper;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Resolver resolver) {
        Class<?> rawClass = javaType.getRawClass();
        // TODO maybe improve with static map and add more time types?
        if (rawClass.equals(Instant.class)) {
            Format format = getFormat(WRITE_DATES_AS_TIMESTAMPS);
            Schema.SchemaBuilder schemaBuilder = createSchemaBuilderWithType(format);
            if (format == ISO8601) {
                return InitialSchema.of(schemaBuilder.format("date-time").build());
            }
            return InitialSchema.of(schemaBuilder.build());
        } else if (rawClass.equals(Duration.class)) {
            Format format = getFormat(WRITE_DURATIONS_AS_TIMESTAMPS);
            Schema.SchemaBuilder schemaBuilder = createSchemaBuilderWithType(format);
            return InitialSchema.of(schemaBuilder.build());
        }
        return null;
    }

    private Format getFormat(SerializationFeature serializationFeature) {
        if (properties.getFormat() == INFER_FROM_OBJECT_MAPPER) {
            if (objectMapper == null) {
                throw new IllegalStateException("Cannot infer format from object mapper without object mapper being present");
            }
            return objectMapper.isEnabled(serializationFeature) ? UNIX_EPOCH_SECONDS : ISO8601;
        }
        return properties.getFormat();
    }

    private static Schema.SchemaBuilder createSchemaBuilderWithType(Format format) {
        if (format == ISO8601) {
            return Schema.builder().type("string");
        } else if (format == UNIX_EPOCH_SECONDS) {
            return Schema.builder().type("number").format("int64");
        }
        throw new IllegalArgumentException("Unsupported Java8 Time Format " + format);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
