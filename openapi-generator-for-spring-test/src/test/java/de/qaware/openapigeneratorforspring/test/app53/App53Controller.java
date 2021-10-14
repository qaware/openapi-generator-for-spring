package de.qaware.openapigeneratorforspring.test.app53;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("base")
@ApiResponse(description = "Base Response", responseCode = "401")
public interface App53Controller {
    @PostMapping("mapping1")
    @ApiResponse(description = "Response 1", responseCode = "400")
    void mapping1(@RequestParam @Parameter(description = "Param1") String param1);

    @PostMapping(value = "mapping2", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ApiResponse(description = "Response 2")
    void mapping2(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Body2") String body);
}
