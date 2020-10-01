package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class InitialTypeResolverForPrimitiveTypes implements InitialTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    private static Supplier<Schema> getSchemaSupplier(String type) {
        return getSchemaSupplier(type, null);
    }

    private static Supplier<Schema> getSchemaSupplier(String type, @Nullable String format) {
        return () -> Schema.builder().type(type).format(format).build();
    }

    private static Supplier<Schema> getStringSchemaSupplier(@Nullable String format) {
        return getSchemaSupplier("string", format);
    }

    private static Supplier<Schema> getIntegerSchemaSupplier(@Nullable String format) {
        return getSchemaSupplier("integer", format);
    }

    private static Supplier<Schema> getNumberSchemaSupplier(@Nullable String format) {
        return getSchemaSupplier("number", format);
    }

    private static final Map<Class<?>, Supplier<Schema>> PRIMITIVE_TYPE_CLASS_TO_SCHEMA;

    static {
        Map<Class<?>, Supplier<Schema>> map = new HashMap<>();
        putValueForKeys(map, getSchemaSupplier("boolean"), Boolean.class, Boolean.TYPE);
        putValueForKeys(map, getStringSchemaSupplier(null), String.class, Character.class, Character.TYPE);
        putValueForKeys(map, getStringSchemaSupplier("byte"), Byte.class, Byte.TYPE);
        putValueForKeys(map, getStringSchemaSupplier("url"), URL.class);
        putValueForKeys(map, getStringSchemaSupplier("uri"), java.net.URI.class);
        putValueForKeys(map, getStringSchemaSupplier("uuid"), java.util.UUID.class);
        putValueForKeys(map, getIntegerSchemaSupplier("int32"), Integer.class, Integer.TYPE, Short.class, Short.TYPE);
        putValueForKeys(map, getIntegerSchemaSupplier("int64"), Long.class, Long.TYPE);
        putValueForKeys(map, getNumberSchemaSupplier("float"), Float.class, Float.TYPE);
        putValueForKeys(map, getNumberSchemaSupplier("double"), Double.class, Double.TYPE);
        putValueForKeys(map, getIntegerSchemaSupplier(null), BigInteger.class);
        putValueForKeys(map, getNumberSchemaSupplier(null), BigDecimal.class, Number.class);
        putValueForKeys(map, getStringSchemaSupplier("binary"), java.io.File.class);
        PRIMITIVE_TYPE_CLASS_TO_SCHEMA = map;
    }

    @SafeVarargs
    private static <K, V> void putValueForKeys(Map<K, V> m, V value, K... keys) {
        for (K key : keys) {
            m.put(key, value);
        }
    }

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType) {
        return Optional.ofNullable(PRIMITIVE_TYPE_CLASS_TO_SCHEMA.get(javaType.getRawClass()))
                .map(Supplier::get)
                .map(InitialSchema::of)
                .orElse(null);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
