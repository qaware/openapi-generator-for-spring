package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.info.License;

import javax.annotation.Nullable;

public interface LicenseAnnotationMapper {
    @Nullable
    License map(io.swagger.v3.oas.annotations.info.License licenseAnnotation);
}
