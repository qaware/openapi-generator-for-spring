package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import reactor.core.publisher.Flux;

import java.util.Map;

public class TypeResolverForFlux implements TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public void resolve(Schema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Map<String, ? extends SchemaProperty> properties,
                        SchemaBuilderFromType schemaBuilderFromType) {
        if (javaType.getRawClass().equals(Flux.class)) {
            JavaType innerType = javaType.getBindings().getTypeParameters().iterator().next();
            schemaBuilderFromType.buildSchemaFromType(innerType, annotationsSupplier, initialSchema::setItems);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }


}
