package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.info.License;

@FunctionalInterface
public interface LicenseAnnotationMapper {
    License map(io.swagger.v3.oas.annotations.info.License licenseAnnotation);
}
