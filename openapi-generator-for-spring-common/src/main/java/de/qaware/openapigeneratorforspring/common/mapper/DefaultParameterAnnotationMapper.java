/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.component.example.ReferencedExamplesConsumer;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver.Mode.FOR_DESERIALIZATION;
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
    public Parameter buildFromAnnotation(io.swagger.v3.oas.annotations.Parameter parameterAnnotation, MapperContext mapperContext) {
        if (StringUtils.isBlank(parameterAnnotation.name())) {
            throw new IllegalStateException("Blank parameter name in parameter annotation");
        }
        Parameter parameter = new Parameter();
        parameter.setName(parameterAnnotation.name());
        applyFromAnnotation(parameter, parameterAnnotation, mapperContext);
        return parameter;
    }

    @Override
    public void applyFromAnnotation(Parameter parameter, io.swagger.v3.oas.annotations.Parameter annotation, MapperContext mapperContext) {
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
        setIf(annotation.explode(), explode -> explode != Explode.DEFAULT, explode -> parameter.setExplode(explode == Explode.TRUE));
        if (annotation.allowReserved()) {
            parameter.setAllowReserved(true);
        }
        ReferencedSchemaConsumer referencedSchemaConsumer = mapperContext.getReferencedItemConsumer(ReferencedSchemaConsumer.class);
        setIfNotEmpty(schemaAnnotationMapper.buildFromAnnotation(FOR_DESERIALIZATION, annotation.schema(), referencedSchemaConsumer),
                schema -> referencedSchemaConsumer.maybeAsReference(schema, parameter::setSchema)
        );
        setMapIfNotEmpty(contentAnnotationMapper.mapArray(annotation.content(), Parameter.class, FOR_DESERIALIZATION, mapperContext), parameter::setContent);
        ReferencedExamplesConsumer referencedExamplesConsumer = mapperContext.getReferencedItemConsumer(ReferencedExamplesConsumer.class);
        setMapIfNotEmpty(exampleObjectAnnotationMapper.mapArray(annotation.examples()),
                examples -> referencedExamplesConsumer.maybeAsReference(examples, parameter::setExamples)
        );
        setStringIfNotBlank(annotation.example(), parameter::setExample);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), parameter::setExtensions);
        setStringIfNotBlank(annotation.ref(), parameter::setRef);
    }
}
