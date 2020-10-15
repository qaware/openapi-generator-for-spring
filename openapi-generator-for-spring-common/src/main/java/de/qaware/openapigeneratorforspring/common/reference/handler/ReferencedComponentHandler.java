package de.qaware.openapigeneratorforspring.common.reference.handler;

import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.OpenApi;

public interface ReferencedComponentHandler extends ReferencedItemHandler {

    @Override
    default void applyToOpenApi(OpenApi openApi) {
        // ReferencedItemSupportFactory ensures that openApi.getComponents() is not null
        applyToComponents(openApi.getComponents());
    }

    void applyToComponents(Components components);
}
