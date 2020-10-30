package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.DependentReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
                List<DependentReferencedComponentHandler> referencedComponentHandlers = itemHandlers.stream()
                        .map(itemHandler -> {
                            if (itemHandler instanceof DependentReferencedComponentHandler) {
                                return (DependentReferencedComponentHandler) itemHandler;
                            }
                            // non-dependent handler can already be handled here
                            itemHandler.applyToOpenApi(openApi);
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                new DependentReferencedComponentHandlersSupport(openApi.getComponents(), referencedComponentHandlers)
                        .handle();

                if (openApi.getComponents().isEmpty()) {
                    openApi.setComponents(null);
                }
            }


        };
    }

    private static class DependentReferencedComponentHandlersSupport {
        private final Components components;
        private final Map<ReferenceType, Pair<DependentReferencedComponentHandler, List<ReferenceType>>> handlersMap;

        public DependentReferencedComponentHandlersSupport(Components components, List<DependentReferencedComponentHandler> handlers) {
            this.components = components;
            this.handlersMap = handlers.stream().map(handler -> {
                Pair<ReferenceType, List<ReferenceType>> buildDependencies = handler.getBuildDependencies();
                return Pair.of(buildDependencies.getKey(), Pair.of(handler, buildDependencies.getValue()));
            }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        }

        public void handle() {
            Map<ReferenceType, Set<ReferenceType>> transitiveDependencies = handlersMap.keySet().stream()
                    .collect(Collectors.toMap(r -> r, r -> buildTransitiveDependencies(r).collect(Collectors.toSet())));

            handlersMap.entrySet().stream()
                    .map(entry -> Pair.of(entry.getKey(), entry.getValue().getLeft()))
                    .sorted(buildDependencyComparator(transitiveDependencies)) // makes less-dependent handlers go first!
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> a, LinkedHashMap::new))
                    .forEach((referenceType, handler) -> {
                        LOGGER.debug("Building components for {}", referenceType);
                        handler.applyToComponents(components);
                    });
        }

        private Stream<ReferenceType> buildTransitiveDependencies(ReferenceType referenceType) {
            List<ReferenceType> dependencies = handlersMap.get(referenceType).getValue();
            return Stream.of(
                    dependencies.stream(), // direct dependencies
                    dependencies.stream()  // together with recursively defined transitive ones
                            .filter(r -> r != referenceType) // some are self-referencing
                            .flatMap(this::buildTransitiveDependencies)
            ).flatMap(x -> x);
        }

        private static Comparator<Pair<ReferenceType, DependentReferencedComponentHandler>> buildDependencyComparator(Map<ReferenceType, Set<ReferenceType>> transitiveDependencies) {
            return (o1, o2) -> {
                // o1 depends on o2
                if (transitiveDependencies.get(o1.getKey()).contains(o2.getKey())) {
                    return 1;
                }
                // o2 depends on o1
                if (transitiveDependencies.get(o2.getKey()).contains(o1.getKey())) {
                    return -1;
                }
                return 0;
            };
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
