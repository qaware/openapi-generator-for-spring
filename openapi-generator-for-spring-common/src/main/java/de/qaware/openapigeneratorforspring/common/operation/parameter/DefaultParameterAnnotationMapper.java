package de.qaware.openapigeneratorforspring.common.operation.parameter;

import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExampleObjectAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiEnumUtils.findEnumByToString;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIf;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultParameterAnnotationMapper implements ParameterAnnotationMapper {

    private final SchemaAnnotationMapper schemaAnnotationMapper;
    private final ContentAnnotationMapper contentAnnotationMapper;
    private final ExampleObjectAnnotationMapper exampleObjectAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Nullable
    @Override
    public Parameter buildFromAnnotation(io.swagger.v3.oas.annotations.Parameter parameterAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer) {
        Parameter parameter = new Parameter();
        applyFromAnnotation(parameter, parameterAnnotation, referencedSchemaConsumer);
        if (parameter.equals(new Parameter())) {
            return null;
        }
        return parameter;
    }

    @Override
    public void applyFromAnnotation(Parameter parameter, io.swagger.v3.oas.annotations.Parameter annotation, ReferencedSchemaConsumer referencedSchemaConsumer) {
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
        setIf(annotation.style(), style -> style != ParameterStyle.DEFAULT, style -> parameter.setStyle(findEnumByToString(style, Parameter.StyleEnum.class)));
        if (annotation.allowReserved()) {
            parameter.setAllowReserved(true);
        }
        schemaAnnotationMapper.buildFromAnnotation(annotation.schema(), referencedSchemaConsumer, parameter::setSchema);
        // TODO handle @ArraySchema as well?
        setMapIfNotEmpty(contentAnnotationMapper.mapArray(annotation.content(), referencedSchemaConsumer), parameter::setContent);
        setMapIfNotEmpty(exampleObjectAnnotationMapper.mapArray(annotation.examples()), parameter::setExamples);
        setStringIfNotBlank(annotation.example(), parameter::setExample);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), parameter::setExtensions);
        setStringIfNotBlank(annotation.ref(), parameter::set$ref);
    }
}
