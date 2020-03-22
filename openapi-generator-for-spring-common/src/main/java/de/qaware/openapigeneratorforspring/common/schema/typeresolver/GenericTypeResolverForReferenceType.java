package de.qaware.openapigeneratorforspring.common.schema.typeresolver;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.SchemaBuilderFromType;
import org.springframework.core.Ordered;

import java.util.function.Consumer;

public class GenericTypeResolverForReferenceType implements GenericTypeResolver {

    public static final int ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        if (javaType.isReferenceType()) {
            schemaBuilderFromType.buildSchemaFromType(javaType.getContentType(), annotationsSupplier, schema -> {
                // TODO check again if all jackson reference types should be considered @Nullable
                schema.setNullable(true);
                schemaConsumer.accept(schema);
            });
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
