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

package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class SchemaPropertyFilterForIgnoredMembers implements SchemaPropertyFilter {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public boolean accept(BeanPropertyDefinition property, BeanDescription beanDescriptionForType, AnnotationsSupplier annotationsSupplierForType, MapperConfig<?> mapperConfig) {
        Set<String> ignored = mapperConfig.getAnnotationIntrospector().findPropertyIgnorals(beanDescriptionForType.getClassInfo()).getIgnored();
        return !ignored.contains(property.getName());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
