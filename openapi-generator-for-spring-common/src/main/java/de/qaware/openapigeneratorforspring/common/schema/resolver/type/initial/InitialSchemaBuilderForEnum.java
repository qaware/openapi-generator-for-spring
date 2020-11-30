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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class InitialSchemaBuilderForEnum implements InitialSchemaBuilder {
    @Nullable
    @Override
    public Schema buildFromType(JavaType javaType) {
        if (javaType.isEnumImplType()) {
            List<Object> enumValues = Arrays.asList(javaType.getRawClass().getEnumConstants());
            return Schema.builder().type("string").enumValues(enumValues).build();
        }
        return null;
    }
}
