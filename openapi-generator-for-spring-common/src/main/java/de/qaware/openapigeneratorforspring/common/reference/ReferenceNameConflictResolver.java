package de.qaware.openapigeneratorforspring.common.reference;


import io.swagger.v3.oas.models.media.Schema;

import java.util.List;

public interface ReferenceNameConflictResolver {
    List<ReferenceName> resolveConflict(List<Schema<Object>> schemasWithSameReferenceName, ReferenceName originalReferenceName);
}
