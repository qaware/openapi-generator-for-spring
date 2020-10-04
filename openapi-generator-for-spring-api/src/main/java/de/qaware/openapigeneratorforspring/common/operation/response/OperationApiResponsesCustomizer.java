package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import org.springframework.core.Ordered;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface OperationApiResponsesCustomizer extends Ordered {
    int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
