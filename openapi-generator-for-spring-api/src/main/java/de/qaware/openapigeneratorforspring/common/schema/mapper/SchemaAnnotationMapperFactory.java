package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Factory for {@link SchemaAnnotationMapper}. Resolved circular bean
 * dependency of {@link SchemaResolver} and {@link SchemaAnnotationMapper},
 * as during mapping of the {@link io.swagger.v3.oas.annotations.media.Schema
 * schema annotation}, nested schema resolution may also happen (for example
 * for {@link Schema#discriminatorMapping() discriminator mappings}).
 */
public interface SchemaAnnotationMapperFactory {
    SchemaAnnotationMapper create(SchemaResolver schemaResolver);
}
