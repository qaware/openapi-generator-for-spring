package de.qaware.openapigeneratorforspring.test.app53;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiResponse(description = "Another base response", responseCode = "499")
class App53ControllerImpl implements App53Controller {

    @Override
    public void mapping1(String param1) {

    }

    @Override
    public void mapping2(String body) {

    }
}
