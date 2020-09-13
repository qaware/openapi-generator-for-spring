package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import io.swagger.v3.oas.models.Paths;

public interface PathsBuilder {
    Paths buildPaths(ReferencedItemConsumerSupplier referencedItemConsumerSupplier);
}
