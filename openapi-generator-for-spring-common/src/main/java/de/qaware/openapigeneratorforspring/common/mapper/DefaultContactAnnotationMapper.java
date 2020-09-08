package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.info.Contact;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultContactAnnotationMapper implements ContactAnnotationMapper {
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Nullable
    @Override
    public Contact map(io.swagger.v3.oas.annotations.info.Contact contactAnnotation) {
        Contact contact = new Contact();
        setStringIfNotBlank(contactAnnotation.name(), contact::setName);
        setStringIfNotBlank(contactAnnotation.url(), contact::setUrl);
        setStringIfNotBlank(contactAnnotation.email(), contact::setEmail);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(contactAnnotation.extensions()), contact::setExtensions);
        // check if anything was set at all
        return contact.equals(new Contact()) ? null : contact;
    }
}
