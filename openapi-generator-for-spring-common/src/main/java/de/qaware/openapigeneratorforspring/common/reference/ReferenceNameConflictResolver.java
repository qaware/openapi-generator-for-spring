package de.qaware.openapigeneratorforspring.common.reference;


import de.qaware.openapigeneratorforspring.common.schema.Schema;

import java.util.List;

public interface ReferenceNameConflictResolver {
    // TODO generalize for other things than Schema
    List<ReferenceName> resolveConflict(List<Schema> schemasWithSameReferenceName, ReferenceName originalReferenceName);
}
