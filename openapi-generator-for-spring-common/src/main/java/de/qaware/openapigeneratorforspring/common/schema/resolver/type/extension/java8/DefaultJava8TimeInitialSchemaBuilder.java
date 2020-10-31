package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaTypeResolver;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format.ISO8601;

@RequiredArgsConstructor
public class DefaultJava8TimeInitialSchemaBuilder implements Java8TimeInitialSchemaBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    private final Java8TimeTypeResolverConfigurationProperties properties;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver fallbackResolver) {
        Class<?> rawClass = javaType.getRawClass();
        Format format = properties.getFormat();
        Schema.SchemaBuilder schemaBuilder = createSchemaBuilderWithType(format);

        // TODO maybe improve with static map and add more types?
        if (rawClass.equals(Instant.class)) {
            if (format == ISO8601) {
                return InitialSchema.of(schemaBuilder.format("date-time").build());
            }
            return InitialSchema.of(schemaBuilder.build());
        } else if (rawClass.equals(Duration.class)) {
            return InitialSchema.of(schemaBuilder.build());
        }
        return null;
    }

    private static Schema.SchemaBuilder createSchemaBuilderWithType(Format format) {
        switch (format) {
            case ISO8601:
                return Schema.builder().type("string");
            case UNIX_EPOCH_SECONDS:
                return Schema.builder().type("number").format("int64");
        }
        throw new IllegalArgumentException("Unsupported Java8 Time Format " + format);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
