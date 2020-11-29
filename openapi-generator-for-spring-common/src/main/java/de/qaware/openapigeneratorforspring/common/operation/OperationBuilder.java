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

package de.qaware.openapigeneratorforspring.common.operation;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.MapperContextImpl;
import de.qaware.openapigeneratorforspring.common.mapper.OperationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.firstNonNull;

@RequiredArgsConstructor
public class OperationBuilder {

    private final OperationAnnotationMapper operationAnnotationMapper;
    private final List<OperationCustomizer> operationCustomizers;
    private final List<HandlerMethod.ContextModifierMapper<MapperContext>> contextModifierMappersForMapperContext;

    public Operation buildOperation(OperationInfo operationInfo, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        try {
            return buildOperationInternal(operationInfo, referencedItemConsumerSupplier);
        } catch (Exception e) {
            // wrap it into exception to help debugging failed open api builds
            throw new OperationBuilderException("Exception encountered while building operation with " + operationInfo, e);
        }
    }

    public Operation buildOperationInternal(OperationInfo operationInfo, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        HandlerMethod handlerMethod = operationInfo.getHandlerMethod();
        OperationBuilderContext operationBuilderContext = OperationBuilderContextImpl.of(operationInfo,
                context -> {
                    MapperContextImpl mapperContext = MapperContextImpl.of(referencedItemConsumerSupplier);
                    return firstNonNull(contextModifierMappersForMapperContext, mapper -> mapper.map(context))
                            .map(modifier -> modifier.modify(mapperContext))
                            .orElse(mapperContext);
                },
                referencedItemConsumerSupplier
        );

        Operation operation = handlerMethod.findAnnotationsWithContext(io.swagger.v3.oas.annotations.Operation.class)
                .map(Pair::of)
                .reduce(new Operation(), (op, annotationWithContext) -> {
                    operationAnnotationMapper.applyFromAnnotation(op, annotationWithContext.getLeft(),
                            operationBuilderContext.getMapperContext(annotationWithContext.getRight())
                    );
                    return op;
                }, (a, b) -> b);

        operationCustomizers.forEach(customizer -> customizer.customize(operation, operationBuilderContext));
        return operation;
    }
}
