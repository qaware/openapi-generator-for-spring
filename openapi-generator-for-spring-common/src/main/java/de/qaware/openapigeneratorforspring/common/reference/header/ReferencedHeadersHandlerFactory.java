package de.qaware.openapigeneratorforspring.common.reference.header;

import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper.HeaderWithOptionalName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemHandlerFactory;
import io.swagger.v3.oas.models.headers.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ReferencedHeadersHandlerFactory implements ReferencedItemHandlerFactory<List<HeaderWithOptionalName>, Map<String, Header>> {
    private final ReferenceDeciderForHeader referenceDecider;
    private final ReferenceNameFactoryForHeader referenceNameFactory;
    private final ReferenceNameConflictResolverForHeader referenceNameConflictResolver;

    @Override
    public ReferencedItemHandler<List<HeaderWithOptionalName>, Map<String, Header>> create() {
        ReferencedHeaderStorage storage = new ReferencedHeaderStorage(referenceDecider, referenceNameFactory, referenceNameConflictResolver);
        return new ReferencedHeadersHandlerImpl(storage, referenceNameFactory);
    }

    @Override
    public ResolvableType getResolvableTypeOfItem() {
        return ResolvableType.forClassWithGenerics(List.class, HeaderWithOptionalName.class);
    }
}
