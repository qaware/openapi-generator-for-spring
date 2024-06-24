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

package de.qaware.openapigeneratorforspring.common.reference.handler;

import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.OpenApi;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class AbstractReferencedComponentHandler implements ReferencedItemHandler {

    @Override
    public final void applyToOpenApi(OpenApi openApi, @Nullable ReferencedItemBuildContext context) {
        // ReferencedItemSupportFactory ensures that openApi.getComponents() is not null
        applyToComponents(
                Objects.requireNonNull(openApi.getComponents(), "components of openApi must not be null"),
                context
        );
    }

    protected abstract void applyToComponents(Components components, @Nullable ReferencedItemBuildContext context);
}
