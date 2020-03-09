package de.qaware.openapigeneratorforspring.common.reference;


import de.qaware.openapigeneratorforspring.common.schema.Schema;

import java.util.List;

public interface ReferenceNameConflictResolver {
    List<ReferenceName> resolveConflict(List<Schema> schemasWithSameReferenceName, ReferenceName originalReferenceName);
}
