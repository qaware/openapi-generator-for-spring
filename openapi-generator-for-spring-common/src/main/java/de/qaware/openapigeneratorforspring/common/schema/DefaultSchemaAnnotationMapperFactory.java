package de.qaware.openapigeneratorforspring.common.schema;

import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultSchemaAnnotationMapperFactory implements SchemaAnnotationMapperFactory {
    private final ParsableValueMapper parsableValueMapper;
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public SchemaAnnotationMapper create(SchemaResolver schemaResolver) {
        return new DefaultSchemaAnnotationMapper(parsableValueMapper, externalDocumentationAnnotationMapper, extensionAnnotationMapper, schemaResolver);
    }
}
