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

import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.model.example.Example;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultExampleObjectAnnotationMapper implements ExampleObjectAnnotationMapper {

    private final ParsableValueMapper parsableValueMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Map<String, Example> mapArray(ExampleObject[] exampleObjectAnnotations) {
        return OpenApiMapUtils.buildStringMapFromStream(Arrays.stream(exampleObjectAnnotations),
                ExampleObject::name,
                this::map
        );
    }

    @Override
    public Example map(ExampleObject exampleObjectAnnotation) {
        Example example = new Example();
        setStringIfNotBlank(exampleObjectAnnotation.summary(), example::setSummary);
        setStringIfNotBlank(exampleObjectAnnotation.description(), example::setDescription);
        setStringIfNotBlank(exampleObjectAnnotation.value(),
                value -> example.setValue(parsableValueMapper.parse(value))
        );
        setStringIfNotBlank(exampleObjectAnnotation.externalValue(), example::setExternalValue);
        setStringIfNotBlank(exampleObjectAnnotation.ref(), example::setRef);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(exampleObjectAnnotation.extensions()), example::setExtensions);
        return example;
    }
}
