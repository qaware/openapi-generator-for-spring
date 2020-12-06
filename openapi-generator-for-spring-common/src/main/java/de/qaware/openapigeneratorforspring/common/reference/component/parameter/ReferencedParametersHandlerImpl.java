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

package de.qaware.openapigeneratorforspring.common.reference.component.parameter;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceType;
import de.qaware.openapigeneratorforspring.common.reference.handler.AbstractDependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemBuildContext;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ReferencedParametersHandlerImpl extends AbstractDependentReferencedComponentHandler implements ReferencedParametersConsumer {
    private final ReferencedParameterStorage storage;

    @With
    @Nullable
    private final Object owner;

    @Override
    public void maybeAsReference(List<Parameter> parameters, Consumer<List<Parameter>> parametersSetter) {
        parametersSetter.accept(parameters);
        storage.maybeReferenceParameters(parameters, owner);
    }

    @Override
    public Pair<ReferenceType, List<ReferenceType>> getBuildDependencies() {
        return storage.getBuildDependencies();
    }

    @Override
    public void applyToComponents(Components components, @Nullable ReferencedItemBuildContext context) {
        storage.buildReferencedItems(components::setParameters, context);
    }
}
