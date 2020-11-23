package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.trait.HasContent;

import java.util.Set;

public interface MediaTypesProvider {
    Set<String> getMediaTypes(Class<? extends HasContent> owningType);
}
