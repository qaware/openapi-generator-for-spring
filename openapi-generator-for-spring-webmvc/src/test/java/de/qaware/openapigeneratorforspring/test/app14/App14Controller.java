package de.qaware.openapigeneratorforspring.test.app14;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class App14Controller {

    // basically follows https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/examples/v3.0/callback-example.yaml
    @PostMapping(value = "/streams", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "subscribes a client to receive out-of-band data")
    @ApiResponse(description = "subscription successfully created")
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
                            @ApiResponse(responseCode = "202",
                                    description = "Your server implementation should return this HTTP status code if the data was received successfully"),
                            @ApiResponse(responseCode = "204",
                                    description = "Your server should return this HTTP status code if no longer interested in further updates")
                    }
            )
    )
    @Callback(
            name = "callback1",
            callbackUrlExpression = "expression1",
            operation = @Operation(method = "get", operationId = "operationId1",
                    responses = @ApiResponse(responseCode = "202", description = "description")
            )
    )
    public SubscriptionDto mapping1(@RequestParam
                                    @Parameter(description = "the location where data will be sent.  Must be network accessible by the source server")
                                    @Schema(format = "uri", example = "https://tonys-server.com") String callbackUrl) {
        return null;
    }

    @GetMapping("mapping2")
    @Callback(
            name = "callback1",
            callbackUrlExpression = "expression1",
            operation = @Operation(method = "get", operationId = "operationId1",
                    responses = @ApiResponse(responseCode = "202", description = "description")
            )
    )
    public void mapping2() {

    }

    @Value
    // TODO make "requiredProperties" also work via "required = true" on properties
    @Schema(description = "subscription information", requiredProperties = "subscriptionId")
    private static class SubscriptionDto {
        @Schema(description = "this unique identifier allows management of the subscription", example = "2531329f-fb09-4ef7-887e-84e648214436")
        String subscriptionId;
    }

    @Value
    private static class PayloadDto {
        Instant timestamp;
        String userData;
    }
}
