package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import io.swagger.v3.oas.models.Paths;

public interface PathsBuilder {
    Paths buildPaths(ReferencedSchemaConsumer referencedSchemaConsumer);
}
