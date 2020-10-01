package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.info.Info;
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
        Info.InfoBuilder infoBuilder = Info.builder();
        setStringIfNotBlank(infoAnnotation.title(), infoBuilder::title);
        setStringIfNotBlank(infoAnnotation.description(), infoBuilder::description);
        setStringIfNotBlank(infoAnnotation.termsOfService(), infoBuilder::termsOfService);
        infoBuilder.contact(contactAnnotationMapper.map(infoAnnotation.contact()));
        infoBuilder.license(licenseAnnotationMapper.map(infoAnnotation.license()));
        setStringIfNotBlank(infoAnnotation.version(), infoBuilder::version);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(infoAnnotation.extensions()), infoBuilder::extensions);
        return infoBuilder.build();
    }
}
