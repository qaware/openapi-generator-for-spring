package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;
import java.util.Map;

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
        @Singular
        private final Map<String, AnnotatedMember> properties;
    }
}
