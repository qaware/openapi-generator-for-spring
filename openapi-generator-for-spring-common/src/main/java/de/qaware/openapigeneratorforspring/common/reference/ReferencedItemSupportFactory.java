package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import lombok.RequiredArgsConstructor;
import lombok.With;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReferencedItemSupportFactory {

    private final List<ReferencedItemHandlerFactory> factories;

    public ReferencedItemSupport create() {

        List<? extends ReferencedItemHandler> itemHandlers = factories.stream()
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
                itemHandlers.forEach(referencedItemHandler -> referencedItemHandler.applyToOpenApi(openApi));
                if (openApi.getComponents().isEmpty()) {
                    openApi.setComponents(null);
                }
            }
        };
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
