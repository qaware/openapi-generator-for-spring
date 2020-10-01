package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.path.Paths;

public interface PathsBuilder {
    Paths buildPaths(ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}
