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

import de.qaware.openapigeneratorforspring.common.reference.component.header.ReferencedHeadersConsumer;
import de.qaware.openapigeneratorforspring.model.media.Encoding;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;


@RequiredArgsConstructor
public class DefaultEncodingAnnotationMapper implements EncodingAnnotationMapper {

    private final HeaderAnnotationMapper headerAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Map<String, Encoding> mapArray(io.swagger.v3.oas.annotations.media.Encoding[] encodingAnnotations, MapperContext mapperContext) {
        return buildStringMapFromStream(
                Arrays.stream(encodingAnnotations),
                io.swagger.v3.oas.annotations.media.Encoding::name,
                encodingAnnotation -> map(encodingAnnotation, mapperContext)
        );
    }

    @Override
    public Encoding map(io.swagger.v3.oas.annotations.media.Encoding encodingAnnotation, MapperContext mapperContext) {
        Encoding encoding = new Encoding();

        setStringIfNotBlank(encodingAnnotation.contentType(), encoding::setContentType);
        setStringIfNotBlank(encodingAnnotation.style(), encoding::setStyle);

        if (encodingAnnotation.explode()) {
            encoding.setExplode(true);
        }
        if (encodingAnnotation.allowReserved()) {
            encoding.setAllowReserved(true);
        }

        setMapIfNotEmpty(headerAnnotationMapper.mapArray(encodingAnnotation.headers(), mapperContext),
                headers -> mapperContext.getReferencedItemConsumer(ReferencedHeadersConsumer.class).maybeAsReference(headers, encoding::setHeaders)
        );
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(encodingAnnotation.extensions()), encoding::setExtensions);

        return encoding;
    }
}
