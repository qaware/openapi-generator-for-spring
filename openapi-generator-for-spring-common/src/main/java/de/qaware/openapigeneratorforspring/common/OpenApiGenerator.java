package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceDecider;
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
    private final OpenApiInfoSupplier openApiInfoSupplier;

    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolver referenceNameConflictResolver;
    private final ReferenceDecider referenceDecider;

    public OpenAPI generateOpenApi() {

        ReferencedSchemaStorage referencedSchemaStorage = new DefaultReferencedSchemaStorage(referenceNameFactory, referenceNameConflictResolver, referenceDecider);

        ReferencedSchemaConsumer referencedSchemaConsumer = new DefaultReferencedSchemaConsumer(referencedSchemaStorage);

        Paths paths = pathsBuilder.buildPaths(referencedSchemaConsumer);

        OpenAPI openApi = new OpenAPI();

        openApi.setPaths(paths); // always set paths, even if empty to comply with spec
        openApi.setInfo(openApiInfoSupplier.get());

        Map<ReferenceName, Schema> referencedSchemas = referencedSchemaStorage.buildReferencedSchemas();

        // TODO fill other referenced stuff, not just schemas
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
