package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

/**
 * Initial schema builder. They are queried in {@link Ordered order} by the {@link
 * de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver
 * schema resolver} to start schema resolution.
 */
@FunctionalInterface
public interface InitialSchemaBuilder extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Build {@link InitialSchema} from given java type.
     * Can return null if that java type cannot be handled.
     *
     * @param javaType            java type
     * @param annotationsSupplier annotations supplier for type
     * @param resolver            fallback resolver which recursively resolves a possibly contained type
     * @return initial schema, or null if java type cannot be handled
     */
    @Nullable
    InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Resolver resolver);

    /**
     * Fallback resolver support for {@link #buildFromType}.
     */
    @FunctionalInterface
    interface Resolver {
        /**
         * Returns an initial schema from the given type.
         *
         * @param javaType            java type
         * @param annotationsSupplier annotations supplier for type
         * @return initial schema, or null if nothing could be built
         */
        @Nullable
        InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier);
    }
}
