package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaStorage;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OpenApiGenerator {


    private final PathsBuilder pathsBuilder;

    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolver referenceNameConflictResolver;

    public OpenAPI generateOpenApi() {

        ReferencedSchemaStorage referencedSchemaStorage = new DefaultReferencedSchemaStorage(referenceNameFactory, referenceNameConflictResolver);

        ReferencedSchemaConsumer referencedSchemaConsumer = new DefaultReferencedSchemaConsumer(referencedSchemaStorage);

        Paths paths = pathsBuilder.buildPaths(referencedSchemaConsumer);

        OpenAPI openApi = new OpenAPI();
        if (!paths.isEmpty()) {
            openApi.setPaths(paths);
        }

        Map<ReferenceName, Schema> referencedSchemas = referencedSchemaStorage.buildReferencedSchemas();

        if (!referencedSchemas.isEmpty()) {
            Components components = new Components();
            Map<String, io.swagger.v3.oas.models.media.Schema> componentsSchemas = referencedSchemas.entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey().asUniqueString(), Map.Entry::getValue));
            components.setSchemas(componentsSchemas);
            openApi.setComponents(components);
        }

        return openApi;
    }


}
