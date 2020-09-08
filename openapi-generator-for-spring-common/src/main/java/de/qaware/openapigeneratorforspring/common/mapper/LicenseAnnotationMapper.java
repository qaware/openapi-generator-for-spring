package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.info.License;

import javax.annotation.Nullable;

public interface LicenseAnnotationMapper {
    @Nullable
    License map(io.swagger.v3.oas.annotations.info.License licenseAnnotation);
}
