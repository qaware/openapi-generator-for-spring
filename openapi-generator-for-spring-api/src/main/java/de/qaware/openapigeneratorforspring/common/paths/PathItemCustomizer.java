package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import org.springframework.core.Ordered;

@FunctionalInterface
public interface PathItemCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    void customize(PathItem pathItem, String pathPattern, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
