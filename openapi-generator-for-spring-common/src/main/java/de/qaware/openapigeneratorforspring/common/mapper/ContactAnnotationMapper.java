package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.info.Contact;

import javax.annotation.Nullable;

public interface ContactAnnotationMapper {
    @Nullable
    Contact map(io.swagger.v3.oas.annotations.info.Contact contactAnnotation);
}
