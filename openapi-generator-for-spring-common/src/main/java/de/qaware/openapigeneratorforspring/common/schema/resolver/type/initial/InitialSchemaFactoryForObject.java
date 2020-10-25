package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class InitialSchemaFactoryForObject implements InitialSchemaFactory {

    // TODO remove!
    public static final Supplier<Schema> OBJECT_SCHEMA_SUPPLIER = () -> Schema.builder().type("object").build();

    // this resolver does not have any condition, so run this always later then the other resolvers as a fallback
    public static final int ORDER = DEFAULT_ORDER + 100;

    private final SchemaNameFactory schemaNameFactory;
    private final SchemaPropertiesResolver schemaPropertiesResolver;

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialTypeResolver fallbackResolver) {
        return InitialSchema.builder()
                .schema(Schema.builder()
                        .type("object")
                        .name(schemaNameFactory.createFromType(javaType))
                        .build()
                )
                .properties(schemaPropertiesResolver.findProperties(javaType))
                .build();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
