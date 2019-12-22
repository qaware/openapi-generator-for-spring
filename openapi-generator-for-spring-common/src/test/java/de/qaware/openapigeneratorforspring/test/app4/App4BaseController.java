package de.qaware.openapigeneratorforspring.test.app4;

import io.swagger.v3.oas.annotations.responses.ApiResponse;


@ApiResponse(responseCode = "503", description = "Something went really wrong from base")
@ApiResponse(responseCode = "210", description = "Really cool response code from base")
public abstract class App4BaseController extends App4BaseBaseController {

}