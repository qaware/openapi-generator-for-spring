package de.qaware.openapigeneratorforspring.common.reference.tag;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReferencedTagsHandlerFactory implements ReferencedItemHandlerFactory {

    @Override
    public ReferencedItemHandler create() {
        return new ReferencedTagsHandlerImpl();
    }
}
