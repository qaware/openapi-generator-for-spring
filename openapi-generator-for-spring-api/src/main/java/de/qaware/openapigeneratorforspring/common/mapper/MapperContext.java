package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.HasReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

/**
 * Context swagger annotations mapping.
 *
 * <p>Enables the following:
 * <ul>
 *
 *  <li>Referencing items such as Schemas and others
 *  including ownership (see implementations of {@link
 *  de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer
 *  ReferencedItemConsumer})</li>
 *
 *  <li>{@link MediaTypesProvider Provides Media types} when mapping {@link
 *  io.swagger.v3.oas.annotations.media.Content content annotation}</li>
 *
 * </ul>
 */
public interface MapperContext extends HasReferencedItemConsumer {

    /**
     * Find the media types for the given owning type having {@link
     * de.qaware.openapigeneratorforspring.model.media.Content content}.
     *
     * @param owningType owning type, must extend {@link de.qaware.openapigeneratorforspring.common.paths.HandlerMethod.HasContext HasContent}
     * @return media types, or empty optional if nothing can be provided for this owning type
     */
    Optional<Set<String>> findMediaTypes(Class<? extends HasContent> owningType);

    /**
     * Set the owner for any following referenced item.
     *
     * @param owner owner, or null if it shall be reset
     * @return mapper context with modified owner
     */
    MapperContext withReferencedItemOwner(@Nullable Object owner);

    /**
     * Sets the {@link MediaTypesProvider media types provider} for any
     * following calls of {@link #findMediaTypes}.
     *
     * @param mediaTypesProvider media types provider
     * @return mapper context with modified media types provider
     */
    MapperContext withMediaTypesProvider(MediaTypesProvider mediaTypesProvider);
}
