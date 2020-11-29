/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.reference.component.schema;

import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.apache.commons.lang3.StringUtils;

public class DefaultReferenceDeciderForSchema implements ReferenceDeciderForSchema {

    @Override
    public boolean turnIntoReference(Schema schema, String referenceIdentifier, long numberOfUsages) {
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
