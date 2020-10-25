package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class TypeResolverForProperties implements TypeResolver {

    // this resolver does not have any condition, so run this always later then the other resolvers as a fallback
    public static final int ORDER = DEFAULT_ORDER + 100;

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public void resolve(
            Schema initialSchema,
            JavaType javaType,
            AnnotationsSupplier annotationsSupplier,
            Map<String, ? extends SchemaProperty> properties,
            SchemaBuilderFromType schemaBuilderFromType
    ) {
        properties.forEach((propertyName, property) -> {
            AnnotatedMember member = property.getAnnotatedMember();
            JavaType propertyType = member.getType();
            AnnotationsSupplier propertyAnnotationsSupplier = annotationsSupplierFactory.createFromMember(member)
                    .andThen(annotationsSupplierFactory.createFromAnnotatedElement(propertyType.getRawClass()));
            schemaBuilderFromType.buildSchemaFromType(propertyType, propertyAnnotationsSupplier,
                    propertySchema -> initialSchema.setProperty(propertyName, property.customize(propertySchema, propertyType, propertyAnnotationsSupplier))
            );
        });
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
