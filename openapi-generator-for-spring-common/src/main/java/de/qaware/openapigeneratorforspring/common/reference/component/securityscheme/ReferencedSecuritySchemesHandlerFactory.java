package de.qaware.openapigeneratorforspring.common.reference.component.securityscheme;

import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import de.qaware.openapigeneratorforspring.common.security.OpenApiSecuritySchemesSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReferencedSecuritySchemesHandlerFactory implements ReferencedItemHandlerFactory {

    private final OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier;
    private final SecuritySchemeAnnotationMapper securitySchemeAnnotationMapper;
    private final List<OpenApiSecuritySchemesSupplier> openApiSecuritySchemesSuppliers;

    @Override
    public ReferencedItemHandler create() {
        return new ReferencedSecuritySchemesHandlerImpl(springBootApplicationAnnotationsSupplier, securitySchemeAnnotationMapper, openApiSecuritySchemesSuppliers);
    }
}
