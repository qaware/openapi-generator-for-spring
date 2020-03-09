package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public class DefaultReferenceNameConflictResolver implements ReferenceNameConflictResolver {

    @Override
    public List<ReferenceName> resolveConflict(List<Schema> schemasWithSameReferenceName, ReferenceName originalReferenceName) {
        List<ReferenceName> referenceNames = new ArrayList<>(schemasWithSameReferenceName.size());
        for (int i = 0; i < schemasWithSameReferenceName.size(); i++) {
            referenceNames.add(originalReferenceName.withIdentifier(originalReferenceName.getIdentifier() + "_" + i));
        }
        return referenceNames;
    }
}
