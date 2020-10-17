package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.mapper.TagAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenAPIDefinitionAnnotationSupplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedTagsHandlerFactory implements ReferencedItemHandlerFactory {

    private final OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier;
    private final TagAnnotationMapper tagAnnotationMapper;

    @Override
    public ReferencedItemHandler create() {
        return new ReferencedTagsHandlerImpl(openAPIDefinitionAnnotationSupplier, tagAnnotationMapper);
    }
}
