package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.core.Ordered;

import java.util.function.Consumer;

/**
 * Generic type resolver for {@link SchemaResolver}. Enables full
 * control the recursive resolution of {@link Schema}s. See also {@link
 * de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver}
 * for non-recursive schema resolution from type.
 */
@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface TypeResolver extends Ordered {

    int DEFAULT_ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    /**
     * Resolve schema from type and provide it
     * via parameter {@code schemaConsumer}.
     *
     * @param javaType                       type to be resolved
     * @param annotationsSupplier            annotations supplier
     * @param schemaConsumer                 schema consumer to provide resolve schema (if any)
     * @param schemaBuilderFromType
     * @param recursiveSchemaBuilderFromType schema builder to recursively build nested schemas
     * @return true if successfully resolved (schema consumer was called), false otherwise
     */
    boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer, SchemaBuilderFromType schemaBuilderFromType, SchemaBuilderFromType recursiveSchemaBuilderFromType);
}
