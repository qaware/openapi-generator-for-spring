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
