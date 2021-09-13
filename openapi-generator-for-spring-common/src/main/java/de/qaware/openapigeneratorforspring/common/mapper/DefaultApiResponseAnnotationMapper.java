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
import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferencedLinksConsumer;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver.Caller.API_RESPONSE;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultApiResponseAnnotationMapper implements ApiResponseAnnotationMapper {
    private final HeaderAnnotationMapper headerAnnotationMapper;
    private final ContentAnnotationMapper contentAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final LinkAnnotationMapper linkAnnotationMapper;

    @Override
    public ApiResponse buildFromAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse apiResponseAnnotation, MapperContext mapperContext) {
        ApiResponse apiResponse = new ApiResponse();
        applyFromAnnotation(apiResponse, apiResponseAnnotation, mapperContext);
        return apiResponse;
    }

    @Override
    public void applyFromAnnotation(ApiResponse apiResponse, io.swagger.v3.oas.annotations.responses.ApiResponse annotation, MapperContext mapperContext) {
        setStringIfNotBlank(annotation.description(), apiResponse::setDescription);
        setMapIfNotEmpty(headerAnnotationMapper.mapArray(annotation.headers(), mapperContext),
                headers -> mapperContext.getReferencedItemConsumer(ReferencedHeadersConsumer.class).maybeAsReference(headers,
                        headersMap -> mergeWithExistingMap(apiResponse::getHeaders, apiResponse::setHeaders, headersMap)
                )
        );
        setMapIfNotEmpty(linkAnnotationMapper.mapArray(annotation.links()),
                links -> mapperContext.getReferencedItemConsumer(ReferencedLinksConsumer.class).maybeAsReference(links,
                        linksMap -> mergeWithExistingMap(apiResponse::getLinks, apiResponse::setLinks, links)
                )
        );
        mergeWithExistingMap(apiResponse::getContent, apiResponse::setContent, contentAnnotationMapper.mapArray(annotation.content(), ApiResponse.class, API_RESPONSE, mapperContext));
        mergeWithExistingMap(apiResponse::getExtensions, apiResponse::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
    }
}
