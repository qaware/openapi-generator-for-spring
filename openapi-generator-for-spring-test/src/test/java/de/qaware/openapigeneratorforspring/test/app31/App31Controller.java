package de.qaware.openapigeneratorforspring.test.app31;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiResponse(responseCode = "500", description = "Error response", content = @Content(schema = @Schema(implementation = App31Controller.ErrorDto.class)))
class App31Controller {

    @PostMapping(value = "/mapping1", consumes = MediaType.TEXT_HTML_VALUE)
    public void mapping1_textHtml(
            @RequestParam String param1,
            @RequestParam String sharedParam,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "request body description") @RequestBody String requestBody
    ) {

    }

    @PostMapping(value = "/mapping1", produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiResponse(description = "some description", content = @Content(schema = @Schema(implementation = String.class)))
    public void mapping1_plain(@RequestParam String param2, @RequestParam String sharedParam) {

    }

    @Value
    static class ErrorDto {
        @Schema(description = "Error code, machine-readable")
        String errorCode;
        @Schema(description = "Error description, human-readable")
        String errorDescription;
    }
}
