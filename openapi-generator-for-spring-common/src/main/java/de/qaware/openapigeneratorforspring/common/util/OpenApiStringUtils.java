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

package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiStringUtils {

    public static void setStringIfNotBlank(@Nullable String value, Consumer<String> setter) {
        OpenApiObjectUtils.setIf(value, StringUtils::isNotBlank, setter);
    }

    public static void setStringRequireNonBlank(@Nullable String value, Consumer<String> setter) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalStateException("String value '" + value + "'  must not be blank");
        }
        setter.accept(value);
    }
}
