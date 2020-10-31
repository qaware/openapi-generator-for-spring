package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Map;

@Builder
@Getter
public class InitialSchema {
    public static InitialSchema of(Schema schema) {
        return InitialSchema.builder()
                .schema(schema)
                .build();
    }

    private final Schema schema;
    @Singular
    private final Map<String, AnnotatedMember> properties;
}
