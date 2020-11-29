package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Generic type resolver for {@link SchemaResolver}. Enables full
 * control the recursive resolution of {@link Schema schemas}
 * and is useful for generic container-like types such as {@link
 * java.util.Collection collections} and schemas carrying properties.
 *
 * <p>Prefer using {@link InitialSchemaBuilder} for
 * extending schema resolution whenever possible.
 *
 * <p>Each implementation typically has a corresponding
 * {@link InitialSchemaBuilder} as a starting point.
 */
@FunctionalInterface
public interface TypeResolver extends Ordered {

    int DEFAULT_ORDER = 0;

    @Nullable
    RecursionKey resolve(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType);

    /**
     * Helper interface for {@link #resolve}. Represents a
     * unique key identifying the built schema, without having
     * the obligation to completely construct the final schema.
     */
    interface RecursionKey {
        boolean equals(Object other);

        int hashCode();
    }

    /**
     * Helper interface for {@link #resolve}.
     */
    interface SchemaBuilderFromType {
        /**
         * Recursively build the nested schema from the given type.
         *
         * @param javaType            nested java type
         * @param annotationsSupplier annotations supplier for type
         * @param schemaConsumer      consumer for schema
         */
        void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer);
    }

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
