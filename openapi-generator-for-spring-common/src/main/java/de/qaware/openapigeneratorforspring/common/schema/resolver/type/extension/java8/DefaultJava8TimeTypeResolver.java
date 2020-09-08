package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format.ISO8601;

@RequiredArgsConstructor
public class DefaultJava8TimeTypeResolver implements Java8TimeTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    private final Java8TimeTypeResolverConfigurationProperties properties;

    @Nullable
    @Override
    public Schema resolveSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        Class<?> rawClass = javaType.getRawClass();
        Format format = properties.getFormat();
        Schema schema = createSchemaWithType(format);

        // TODO maybe improve with static map and add more types?
        if (rawClass.equals(Instant.class)) {
            if (format == ISO8601) {
                return schema.format("date-time");
            }
            return schema;
        } else if (rawClass.equals(Duration.class)) {
            return schema;
        }
        return null;
    }

    private static Schema createSchemaWithType(Format format) {
        switch (format) {
            case ISO8601:
                return new Schema().type("string");
            case UNIX_EPOCH_SECONDS:
                return new Schema().type("number").format("int64");
        }
        throw new IllegalArgumentException("Unsupported Java8 Time Format " + format);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
