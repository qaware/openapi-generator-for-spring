package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultLicenseAnnotationMapper implements LicenseAnnotationMapper {

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Nullable
    @Override
    public License map(io.swagger.v3.oas.annotations.info.License licenseAnnotation) {
        License license = new License();
        setStringIfNotBlank(licenseAnnotation.name(), license::setName);
        setStringIfNotBlank(licenseAnnotation.url(), license::setUrl);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(licenseAnnotation.extensions()), license::setExtensions);
        // check if anything was set at all
        return license.equals(new License()) ? null : license;
    }
}
