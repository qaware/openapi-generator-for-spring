package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import org.springframework.core.Ordered;

import java.util.function.Consumer;

/**
 * Generic type resolver for {@link DefaultSchemaResolver}. Enables full
 * control the recursive resolution of {@link Schema}s. See also {@link
 * AbstractTypeResolver} for non-recursive schema resolution from type.
 */
@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface TypeResolver extends Ordered {

    int DEFAULT_ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    /**
     * Resolve schema from type and provide it
     * via parameter {@code schemaConsumer}.
     *
     * @param javaType              type to be resolved
     * @param annotationsSupplier   annotations supplier
     * @param schemaBuilderFromType schema builder to recursively build nested schemas
     * @param schemaConsumer        schema consumer to provide resolve schema (if any)
     * @return true if successfully resolved (schema consumer was called), false otherwise
     */
    boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer);
}
