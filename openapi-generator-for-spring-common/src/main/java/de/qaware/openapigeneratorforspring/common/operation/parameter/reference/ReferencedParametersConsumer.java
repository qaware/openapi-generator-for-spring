package de.qaware.openapigeneratorforspring.common.operation.parameter.reference;

import de.qaware.openapigeneratorforspring.common.reference.fortype.ReferencedItemConsumerForType;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.List;

public interface ReferencedParametersConsumer extends ReferencedItemConsumerForType<List<Parameter>, List<Parameter>> {
}
