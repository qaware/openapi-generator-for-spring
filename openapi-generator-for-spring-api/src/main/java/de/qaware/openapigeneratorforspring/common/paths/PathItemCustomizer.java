package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.path.PathItem;
import org.springframework.core.Ordered;

public interface PathItemCustomizer extends Ordered {
    int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    void customize(PathItem pathItem, String pathPattern, ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}
