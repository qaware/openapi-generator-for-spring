package de.qaware.openapigeneratorforspring.webmvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.qaware.openapigeneratorforspring.common.AbstractOpenApiResource;
import de.qaware.openapigeneratorforspring.common.OpenApiGenerator;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiResourceForWebMvc extends AbstractOpenApiResource {

    public OpenApiResourceForWebMvc(OpenApiGenerator openApiGenerator, OpenApiObjectMapperSupplier objectMapperSupplier) {
        super(openApiGenerator, objectMapperSupplier);
    }

    @Override
    @GetMapping(value = API_DOCS_PATH_SPEL, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOpenApiAsJson() throws JsonProcessingException {
        return super.getOpenApiAsJson();
    }
}
