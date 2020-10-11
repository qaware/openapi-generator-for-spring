package de.qaware.openapigeneratorforspring.common.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.Components;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.ResolvableType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ReferencedItemSupportFactory {

    private final List<ReferencedItemHandlerFactory<?, ?>> factories;

    public ReferencedItemSupport create() {

        List<Pair<ResolvableType, ? extends ReferencedItemHandler<?, ?>>> itemHandlersByItemClass = factories.stream()
                .map(factory -> Pair.of(factory.getResolvableTypeOfItem(), factory.create()))
                .collect(Collectors.toList());

        return new ReferencedItemSupport() {
            @Override
            public ReferencedItemConsumerSupplier getReferencedItemConsumerSupplier() {
                return new ReferencedItemConsumerSupplierImpl(itemHandlersByItemClass, null);
            }

            @Override
            public Components buildComponents() {
                Components components = new Components();
                itemHandlersByItemClass.stream().map(Pair::getValue)
                        .forEach(referencedItemHandler -> referencedItemHandler.applyToComponents(components));
                return components;
            }
        };
    }

    @RequiredArgsConstructor
    private static class ReferencedItemConsumerSupplierImpl implements ReferencedItemConsumerSupplier {
        private final List<Pair<ResolvableType, ? extends ReferencedItemHandler<?, ?>>> itemHandlersByItemClass;

        @With
        @Nullable
        private final Object owner;

        @Override
        public <T extends ReferencedItemConsumerForType<?, ?>> T get(Class<T> consumerClazz) {
            ResolvableType resolvableTypeForItem = Arrays.stream(ResolvableType.forClass(consumerClazz).getInterfaces())
                    .filter(resolvableType -> ReferencedItemConsumerForType.class.equals(resolvableType.resolve()))
                    .map(resolvableType -> Arrays.stream(resolvableType.getGenerics()).findFirst())
                    .flatMap(optionalGeneric -> optionalGeneric.map(Stream::of).orElse(Stream.empty()))
                    .reduce((a, b) -> {
                        throw new IllegalStateException("Found more than one first generic argument: " + a + " vs. " + b);
                    })
                    .orElseThrow(() -> new IllegalStateException("Cannot find first generic argument on " + consumerClazz));
            ReferencedItemHandler<?, ?> referencedItemHandler = itemHandlersByItemClass.stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(resolvableTypeForItem))
                    .reduce((a, b) -> {
                        throw new IllegalStateException("Found more than one handler for " + resolvableTypeForItem);
                    })
                    .map(Map.Entry::getValue)
                    .orElseThrow(() -> new IllegalStateException("Found no handler for " + resolvableTypeForItem));
            if (!consumerClazz.isInstance(referencedItemHandler)) {
                throw new IllegalStateException("Referenced item handler " + referencedItemHandler.getClass().getSimpleName() + " is not instance of " + consumerClazz);
            }
            return consumerClazz.cast(referencedItemHandler.withOwner(owner));
        }
    }
}
