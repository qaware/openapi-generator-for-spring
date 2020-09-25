package de.qaware.openapigeneratorforspring.common.reference.header;

import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper.HeaderWithOptionalName;
import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import io.swagger.v3.oas.models.headers.Header;

import java.util.List;
import java.util.Map;

public interface ReferencedHeadersConsumer extends ReferencedItemConsumerForType<List<HeaderWithOptionalName>, Map<String, Header>> {

}
