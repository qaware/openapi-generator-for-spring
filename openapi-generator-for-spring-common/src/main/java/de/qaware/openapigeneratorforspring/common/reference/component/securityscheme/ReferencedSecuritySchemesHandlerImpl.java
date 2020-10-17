package de.qaware.openapigeneratorforspring.common.reference.component.securityscheme;

import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.common.security.OpenApiSecuritySchemesSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.ensureKeyIsNotBlank;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

public class ReferencedSecuritySchemesHandlerImpl implements ReferencedComponentHandler, ReferencedSecuritySchemesConsumer {

    private final Map<String, SecurityScheme> securitySchemes;

    public ReferencedSecuritySchemesHandlerImpl(
            OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier,
            SecuritySchemeAnnotationMapper securitySchemeAnnotationMapper,
            List<OpenApiSecuritySchemesSupplier> openApiSecuritySchemesSuppliers
    ) {
        this.securitySchemes = Stream.concat(
                buildStringMapFromStream(
                        springBootApplicationAnnotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.security.SecurityScheme.class),
                        io.swagger.v3.oas.annotations.security.SecurityScheme::name,
                        securitySchemeAnnotationMapper::map
                ).entrySet().stream(),
                openApiSecuritySchemesSuppliers.stream()
                        .map(Supplier::get)
                        .map(Map::entrySet)
                        .flatMap(Collection::stream)
        ).collect(Collectors.toMap(ensureKeyIsNotBlank(Map.Entry::getKey), Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    @Override
    public void accept(Map<String, SecurityScheme> map) {
        map.forEach((name, securityScheme) -> securitySchemes.merge(name, securityScheme, (a, b) -> {
            if (a.equals(b)) {
                return a;
            }
            throw new IllegalStateException("Conflicting security schemes with same name found: " + a + " vs. " + b);
        }));
    }

    @Override
    public void applyToComponents(Components components) {
        setMapIfNotEmpty(securitySchemes, components::setSecuritySchemes);
    }
}
