package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface InitialTypeResolver extends Ordered {

    int DEFAULT_ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    @Nullable
    InitialSchema resolveFromType(JavaType javaType);

    @Builder
    @Getter
    class InitialSchema {
        public static InitialSchema of(Schema schema) {
            return InitialSchema.builder()
                    .schema(schema)
                    .build();
        }

        private final Schema schema;
        private final boolean hasNestedProperties;
    }
}
