package de.qaware.openapigeneratorforspring.common.mapper;


import de.qaware.openapigeneratorforspring.model.info.Contact;

@FunctionalInterface
public interface ContactAnnotationMapper {
    Contact map(io.swagger.v3.oas.annotations.info.Contact contactAnnotation);
}
