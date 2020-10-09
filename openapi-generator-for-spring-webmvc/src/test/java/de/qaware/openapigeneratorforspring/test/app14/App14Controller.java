package de.qaware.openapigeneratorforspring.test.app14;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class App14Controller {

    @PostMapping("/mapping-1")
    @Callback(
            name = "onData",
            callbackUrlExpression = "{$request.query.callbackUrl}/data",
            operation = @Operation(
                    method = "post",
                    requestBody = @RequestBody(
                            description = "subscription payload",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PayloadDto.class)
                            )
                    ),
                    responses = {
                            @ApiResponse(responseCode = "200", description = "OK")
                    }
            )
    )
    public void mapping1() {

    }

    @Value
    private static class PayloadDto {
        Instant timestamp;
        String userData;
    }
}
