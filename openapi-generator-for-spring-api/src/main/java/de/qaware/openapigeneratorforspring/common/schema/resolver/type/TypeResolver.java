package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

/**
 * Generic type resolver for {@link SchemaResolver}. Enables full
 * control the recursive resolution of {@link Schema}s. See also {@link
 * InitialSchemaBuilder}
 * for non-recursive schema resolution from type.
 */
@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
@FunctionalInterface
public interface TypeResolver extends Ordered {

    int DEFAULT_ORDER = 0;

    @Nullable
    RecursionKey resolve(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType);

    interface RecursionKey {
        boolean equals(Object other);

        int hashCode();
    }

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
