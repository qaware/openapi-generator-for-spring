package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.apache.commons.lang3.StringUtils;

public class DefaultReferenceDeciderForSchema implements ReferenceDeciderForSchema {

    @Override
    public boolean turnIntoReference(Schema schema, long numberOfUsages) {
        if (onlyTypeIsSet(schema)) {
            return false;
        }
        if (StringUtils.isNotBlank(schema.getName())) {
            return true;
        }
        return numberOfUsages > 1;
    }

    private boolean onlyTypeIsSet(Schema schema) {
        // check if the schema becomes "empty" once the type is also null
        // this is a little trick to avoid checking all other properties!
        return schema.withType(null).isEmpty();
    }
}
