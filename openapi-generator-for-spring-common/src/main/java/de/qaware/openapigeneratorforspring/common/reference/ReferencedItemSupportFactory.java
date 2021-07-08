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

package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.AbstractDependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemBuildContext;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class ReferencedItemSupportFactory {

    private final List<ReferencedItemHandlerFactory> factories;

    public ReferencedItemSupport create() {

        List<ReferencedItemHandler> itemHandlers = factories.stream()
                .map(ReferencedItemHandlerFactory::create)
                .collect(Collectors.toList());

        return new ReferencedItemSupport() {
            @Override
            public ReferencedItemConsumerSupplier getReferencedItemConsumerSupplier() {
                return new ReferencedItemConsumerSupplierImpl(itemHandlers, null);
            }

            @Override
            public void applyToOpenApi(OpenApi openApi) {
                if (openApi.getComponents() == null) {
                    openApi.setComponents(new Components());
                }
                List<AbstractDependentReferencedComponentHandler> referencedComponentHandlers = itemHandlers.stream()
                        .map(itemHandler -> {
                            if (itemHandler instanceof AbstractDependentReferencedComponentHandler) {
                                return (AbstractDependentReferencedComponentHandler) itemHandler;
                            }
                            // non-dependent handler can already be handled here
                            itemHandler.applyToOpenApi(openApi, null);
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                new DependentReferencedComponentHandlersSupport(openApi, referencedComponentHandlers)
                        .handle();

                if (openApi.getComponents().isEmpty()) {
                    openApi.setComponents(null);
                }
            }


        };
    }

    private static class DependentReferencedComponentHandlersSupport {
        private final OpenApi openApi;
        private final Map<ReferenceType, HandlerWithDependencies> handlersMap;

        public DependentReferencedComponentHandlersSupport(OpenApi openApi, List<AbstractDependentReferencedComponentHandler> handlers) {
            this.openApi = openApi;
            this.handlersMap = handlers.stream()
                    .map(HandlerWithDependencies::of)
                    .collect(Collectors.toMap(HandlerWithDependencies::getType, x -> x, (a, b) -> {
                        throw new IllegalStateException("Found more than one handler for handler for type " + a + " vs. " + b);
                    }));
        }

        public void handle() {
            Map<ReferenceType, Set<ReferenceType>> transitiveDependencies = handlersMap.keySet().stream()
                    .collect(Collectors.toMap(r -> r, r -> buildTransitiveDependencies(r).collect(Collectors.toSet())));

            ReferencedItemBuildContext buildContext = AbstractReferencedItemStorage.createContext();
            handlersMap.values().stream()
                    .sorted(buildDependencyComparator(transitiveDependencies)) // makes less-dependent handlers go first!
                    .forEach(handler -> {
                        LOGGER.debug("Building components for {}", handler.getType());
                        handler.getHandler().applyToOpenApi(openApi, buildContext);
                    });
        }

        private Stream<ReferenceType> buildTransitiveDependencies(ReferenceType referenceType) {
            List<ReferenceType> dependencies = handlersMap.get(referenceType).getDirectDependencies();
            return Stream.of(
                    dependencies.stream(), // direct dependencies
                    dependencies.stream()  // together with recursively defined transitive ones
                            .filter(r -> r != referenceType) // some are self-referencing
                            .flatMap(this::buildTransitiveDependencies)
            ).flatMap(x -> x);
        }

        private static Comparator<HandlerWithDependencies> buildDependencyComparator(Map<ReferenceType, Set<ReferenceType>> transitiveDependencies) {
            return (o1, o2) -> {
                // o1 depends on o2
                if (transitiveDependencies.get(o1.getType()).contains(o2.getType())) {
                    return 1;
                }
                // o2 depends on o1
                if (transitiveDependencies.get(o2.getType()).contains(o1.getType())) {
                    return -1;
                }
                return Comparator.<ReferenceType>naturalOrder().compare(o1.getType(), o2.getType());
            };
        }

        @RequiredArgsConstructor
        @Getter
        private static class HandlerWithDependencies {
            private final AbstractDependentReferencedComponentHandler handler;
            private final ReferenceType type;
            private final List<ReferenceType> directDependencies;

            public static HandlerWithDependencies of(AbstractDependentReferencedComponentHandler handler) {
                Pair<ReferenceType, List<ReferenceType>> buildDependencies = handler.getBuildDependencies();
                return new HandlerWithDependencies(handler, buildDependencies.getKey(), buildDependencies.getValue());
            }
        }
    }

    @RequiredArgsConstructor
    private static class ReferencedItemConsumerSupplierImpl implements ReferencedItemConsumerSupplier {
        private final List<? extends ReferencedItemHandler> itemHandlersByItemClass;

        @With
        @Nullable
        private final Object owner;

        @Override
        public <T extends ReferencedItemConsumer> T get(Class<T> consumerClazz) {
            return itemHandlersByItemClass.stream()
                    .filter(handler -> consumerClazz.isAssignableFrom(handler.getClass()))
                    .map(handler -> handler.withOwner(owner))
                    .map(consumerClazz::cast)
                    .reduce((a, b) -> {
                        throw new IllegalStateException("Found more than one handler for consumer " + consumerClazz);
                    })
                    .orElseThrow(() -> new IllegalStateException("Found no handler for consumer " + consumerClazz));
        }
    }
}
