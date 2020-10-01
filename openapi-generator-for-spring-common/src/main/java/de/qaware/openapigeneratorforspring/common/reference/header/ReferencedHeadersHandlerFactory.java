package de.qaware.openapigeneratorforspring.common.reference.header;

import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper.HeaderWithOptionalName;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.model.header.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ReferencedHeadersHandlerFactory implements ReferencedItemHandlerFactory<List<HeaderWithOptionalName>, Map<String, Header>> {
    private final ReferenceDeciderForHeader referenceDecider;
    private final ReferenceIdentifierFactoryForHeader referenceIdentifierFactory;
    private final ReferenceIdentifierConflictResolverForHeader referenceIdentifierConflictResolver;

    @Override
    public ReferencedItemHandler<List<HeaderWithOptionalName>, Map<String, Header>> create() {
        ReferencedHeaderStorage storage = new ReferencedHeaderStorage(
                referenceDecider,
                (header, headerName) -> Collections.singletonList(referenceIdentifierFactory.buildSuggestedIdentifier(headerName)),
                referenceIdentifierConflictResolver
        );
        return new ReferencedHeadersHandlerImpl(storage, referenceIdentifierFactory);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forClassWithGenerics(List.class, HeaderWithOptionalName.class);
    }
}
