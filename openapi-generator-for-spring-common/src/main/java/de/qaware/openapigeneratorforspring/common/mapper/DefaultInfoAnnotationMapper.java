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

import de.qaware.openapigeneratorforspring.model.info.Info;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultInfoAnnotationMapper implements InfoAnnotationMapper {
    private final ContactAnnotationMapper contactAnnotationMapper;
    private final LicenseAnnotationMapper licenseAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Info map(io.swagger.v3.oas.annotations.info.Info infoAnnotation) {
        Info info = new Info();
        setStringIfNotBlank(infoAnnotation.title(), info::setTitle);
        setStringIfNotBlank(infoAnnotation.description(), info::setDescription);
        setStringIfNotBlank(infoAnnotation.termsOfService(), info::setTermsOfService);
        setIfNotEmpty(contactAnnotationMapper.map(infoAnnotation.contact()), info::setContact);
        setIfNotEmpty(licenseAnnotationMapper.map(infoAnnotation.license()), info::setLicense);
        setStringIfNotBlank(infoAnnotation.version(), info::setVersion);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(infoAnnotation.extensions()), info::setExtensions);
        return info;
    }
}
