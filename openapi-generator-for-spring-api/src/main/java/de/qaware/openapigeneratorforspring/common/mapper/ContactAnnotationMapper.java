package de.qaware.openapigeneratorforspring.common.mapper;


import de.qaware.openapigeneratorforspring.model.info.Contact;

import javax.annotation.Nullable;

public interface ContactAnnotationMapper {
    @Nullable
    Contact map(io.swagger.v3.oas.annotations.info.Contact contactAnnotation);
}
