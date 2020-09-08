package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
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
        info.setContact(contactAnnotationMapper.map(infoAnnotation.contact()));
        info.setLicense(licenseAnnotationMapper.map(infoAnnotation.license()));
        setStringIfNotBlank(infoAnnotation.version(), info::setVersion);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(infoAnnotation.extensions()), info::setExtensions);
        return info;
    }
}
