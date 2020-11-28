package de.qaware.openapigeneratorforspring.common.reference.handler;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface DependentReferencedComponentHandler extends ReferencedComponentHandler {
    Pair<ReferenceType, List<ReferenceType>> getBuildDependencies();

    @Override
    default void applyToOpenApi(OpenApi openApi) {
        // do nothing, as ReferencedItemSupportFactory will dispatch directly applyToComponents
    }
}
