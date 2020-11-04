package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationServersCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final ServerAnnotationMapper serverAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        List<Server> servers = Stream.concat(
                operation.getServers() == null ? Stream.empty() : operation.getServers().stream(),
                collectServersFromMethodAndClass(operationBuilderContext)
        )
                .distinct()
                .collect(Collectors.toList());
        setCollectionIfNotEmpty(servers, operation::setServers);
    }

    private Stream<Server> collectServersFromMethodAndClass(OperationBuilderContext operationBuilderContext) {
        AnnotationsSupplier annotationsSupplier = operationBuilderContext.getOperationInfo().getHandlerMethod().getAnnotationsSupplier();
        return annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.servers.Server.class).map(serverAnnotationMapper::map);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
