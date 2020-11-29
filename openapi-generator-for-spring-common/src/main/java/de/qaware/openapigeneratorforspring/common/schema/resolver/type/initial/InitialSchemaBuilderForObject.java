package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverSupport;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.laterThan;

@RequiredArgsConstructor
public class InitialSchemaBuilderForObject implements InitialSchemaBuilder, TypeResolverSupport {

    // this resolver does not have any condition, so run this always later then the other resolvers as a fallback
    public static final int ORDER = laterThan(DEFAULT_ORDER);

    private final SchemaNameBuilder schemaNameBuilder;
    private final SchemaPropertiesResolver schemaPropertiesResolver;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver resolver) {
        return InitialSchema.builder()
                .schema(Schema.builder()
                        .type("object")
                        .name(schemaNameBuilder.buildFromType(javaType))
                        .build()
                )
                .properties(schemaPropertiesResolver.findProperties(javaType))
                .build();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public boolean supports(InitialSchema initialSchema) {
        return !initialSchema.getProperties().isEmpty();
    }
}
