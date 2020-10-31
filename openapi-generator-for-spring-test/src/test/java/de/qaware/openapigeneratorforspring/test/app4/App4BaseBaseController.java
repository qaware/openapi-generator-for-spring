package de.qaware.openapigeneratorforspring.test.app4;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@ApiResponses(value = {
        @ApiResponse(responseCode = "410", description = "Some more responses 1 from base base"),
        @ApiResponse(responseCode = "411", description = "Some more responses 2 from base base"),
})
public abstract class App4BaseBaseController {

}