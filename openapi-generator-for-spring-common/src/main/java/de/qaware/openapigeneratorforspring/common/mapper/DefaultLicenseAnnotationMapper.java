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

import de.qaware.openapigeneratorforspring.model.info.License;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultLicenseAnnotationMapper implements LicenseAnnotationMapper {

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public License map(io.swagger.v3.oas.annotations.info.License licenseAnnotation) {
        License license = new License();
        setStringIfNotBlank(licenseAnnotation.name(), license::setName);
        setStringIfNotBlank(licenseAnnotation.url(), license::setUrl);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(licenseAnnotation.extensions()), license::setExtensions);
        return license;
    }
}
