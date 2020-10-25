package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactory;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

/**
 * Generic type resolver for {@link SchemaResolver}. Enables full
 * control the recursive resolution of {@link Schema}s. See also {@link
 * InitialSchemaFactory}
 * for non-recursive schema resolution from type.
 */
@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface TypeResolver extends Ordered {

    int DEFAULT_ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    @Nullable
    Object resolve(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType);
}
