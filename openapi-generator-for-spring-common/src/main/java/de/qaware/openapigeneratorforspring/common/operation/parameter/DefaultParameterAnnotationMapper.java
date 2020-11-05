package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferencedExamplesConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIf;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultParameterAnnotationMapper implements ParameterAnnotationMapper {

    private final SchemaAnnotationMapper schemaAnnotationMapper;
    private final ContentAnnotationMapper contentAnnotationMapper;
    private final ExampleObjectAnnotationMapper exampleObjectAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Nullable
    @Override
    public Parameter buildFromAnnotation(io.swagger.v3.oas.annotations.Parameter parameterAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        Parameter parameter = new Parameter();
        applyFromAnnotation(parameter, parameterAnnotation, referencedItemConsumerSupplier);
        if (parameter.isEmpty()) {
            return null;
        }
        return parameter;
    }

    @Override
    public void applyFromAnnotation(Parameter parameter, io.swagger.v3.oas.annotations.Parameter annotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        setStringIfNotBlank(annotation.name(), parameter::setName);
        setStringIfNotBlank(annotation.in().toString(), parameter::setIn);
        setStringIfNotBlank(annotation.description(), parameter::setDescription);
        if (annotation.required()) {
            parameter.setRequired(true);
        }
        if (annotation.deprecated()) {
            parameter.setDeprecated(true);
        }
        if (annotation.allowEmptyValue()) {
            parameter.setAllowEmptyValue(true);
        }
        setIf(annotation.style(), style -> style != ParameterStyle.DEFAULT, style -> parameter.setStyle(style.toString()));
        if (annotation.allowReserved()) {
            parameter.setAllowReserved(true);
        }
        ReferencedSchemaConsumer referencedSchemaConsumer = referencedItemConsumerSupplier.get(ReferencedSchemaConsumer.class);
        setIfNotEmpty(schemaAnnotationMapper.buildFromAnnotation(annotation.schema(), referencedSchemaConsumer),
                schema -> referencedSchemaConsumer.maybeAsReference(schema, parameter::setSchema)
        );
        // TODO handle @ArraySchema as well?
        setMapIfNotEmpty(contentAnnotationMapper.mapArray(annotation.content(), referencedItemConsumerSupplier), parameter::setContent);
        ReferencedExamplesConsumer referencedExamplesConsumer = referencedItemConsumerSupplier.get(ReferencedExamplesConsumer.class);
        setMapIfNotEmpty(exampleObjectAnnotationMapper.mapArray(annotation.examples()),
                examples -> referencedExamplesConsumer.maybeAsReference(examples, parameter::setExamples)
        );
        setStringIfNotBlank(annotation.example(), parameter::setExample);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), parameter::setExtensions);
        setStringIfNotBlank(annotation.ref(), parameter::setRef);
    }
}
