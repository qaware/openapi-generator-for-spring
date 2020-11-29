package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.trait.HasContent;

import java.util.Set;

/**
 * Helper interface for {@link MapperContext} in
 * conjunction with {@link ContentAnnotationMapper}.
 */
@FunctionalInterface
public interface MediaTypesProvider {

    Set<String> getMediaTypes(Class<? extends HasContent> owningType);
}
